package com.example.paymentapi.controller;

import com.example.paymentapi.dto.MemberFormDto;
import com.example.paymentapi.dto.OrderSaveDto;
import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.Member;
import com.example.paymentapi.repository.CartRepository;
import com.example.paymentapi.repository.MemberRepository;
import com.example.paymentapi.repository.PostRepository;
import com.example.paymentapi.service.CartService;
import com.example.paymentapi.service.PostService;
import com.example.paymentapi.web.SessionConst;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final PostService postService;
    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PostRepository postRepository;
    private IamportClient iamportClient;

    @Value("${spring.imp.api.key}")
    private String apiKey;

    @Value("${spring.imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("formDto",  new MemberFormDto());
        return "demo/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@Valid MemberFormDto formDto, BindingResult result) {
        if (result.hasErrors()) {
            return "demo/signup";
        }
        Member member = new Member();
        member.setNickname(formDto.getNickname());
        member.setUsername(formDto.getUserid());
        member.setUserpw(formDto.getUserpw());
        member.setAdress(formDto.getAdr());
        Cart cart = new Cart();
        cart.setMember(member);
        memberRepository.save(member);
        cartRepository.save(cart);
        return "redirect:/demo/list";
    }

    @GetMapping("/signin")
    public String signin() {
        return "demo/signin";
    }

    @PostMapping("/signin")
    public String signinPost(@RequestParam("username") String userid, @RequestParam("password") String userpw, HttpServletRequest request) {
        Optional<Member> member = memberRepository.findByUsername(userid);
        if (member.isPresent() && Objects.equals(member.get().getUserpw(), userpw)) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, member.get());
            return "redirect:/demo/list";
        }
        return "redirect:/demo/signin";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/demo/list";
    }

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pagenum", defaultValue = "0") int pagenum,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember
    ) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagenum", pagenum);
        SearchCondition condition = new SearchCondition();

        if (keyword != null) {
            condition.setItemname(keyword);
            condition.setSeller(keyword);
        }

        PageRequest pageRequest = PageRequest.of(pagenum, 8);
        Page<PostDto> postDto = postRepository.searchPage(condition, pageRequest);
        model.addAttribute("postDto", postDto);

        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
            model.addAttribute("member", member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }
        model.addAttribute("cart", cart);
        if (cart != null && cart.getSavedItems() != null)
            model.addAttribute("cartCount", cart.getSavedItems().size());
        return "demo/list";
    }

    @GetMapping("/item-detail/{id}")
    public String item(
            @PathVariable("id") Long id,
            Model model,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember
    ) {
        PostDto post = postService.read(id);
        model.addAttribute("postDto", post);

        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
            model.addAttribute("member", member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }
        model.addAttribute("cart", cart);
        if (cart != null && cart.getSavedItems() != null)
            model.addAttribute("cartCount", cart.getSavedItems().size());
        return "demo/item-detail";
    }

    @ResponseBody
    @PostMapping("/item-post")
    public PostDto itemToCart(@RequestBody PostDto postDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
        }
        cartService.insert(cart, postService.dtoToEntity(postDto), postDto.getQuantity());
        return postDto;
    }

    @GetMapping("/cart")
    public String cart(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
            model.addAttribute("member", member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }
        model.addAttribute("cart", cart);
        if (cart != null && cart.getSavedItems() != null)
            model.addAttribute("cartCount", cart.getSavedItems().size());
        return "demo/cart";
    }

    @ResponseBody
    @PostMapping("/deletecart")
    public void deleteCart(@RequestParam("itemId") Long itemId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
        }
        cartService.delete(cart, itemId);
    }

    @ResponseBody
    @PostMapping("/updatecart")
    public void updateCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") Long quantity, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
        }
        cartService.update(cart, itemId, quantity);
    }

    @GetMapping("/purchase")
    public String purchase(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member member = postService.exMember(); // create member
            cart = postService.exCart(member);
            model.addAttribute("member", member);
        } else {
            Optional<Cart> optCart = cartRepository.findByMember(sessionMember);
            cart = optCart.orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }
        model.addAttribute("cart", cart);
        if (cart != null && cart.getSavedItems() != null)
            model.addAttribute("cartCount", cart.getSavedItems().size());
        return "demo/purchase";
    }

    @ResponseBody
    @PostMapping("/purchase")
    public void purchase(@RequestParam("memberId") Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        member.ifPresent(cartService::purchaseAll);
    }

    @PostMapping("/order/payment")
    public ResponseEntity<String> paymentComplete(@SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember, @RequestBody List<OrderSaveDto> orderSaveDtos) throws IOException {
        String orderNumber = String.valueOf(orderSaveDtos.get(0).getOrderNumber());
        try {
            if(sessionMember!=null) {
                cartService.purchaseAll(sessionMember);
                System.out.println("결제 성공 : 주문 번호 {}" + orderNumber);
                return ResponseEntity.ok().build();
            }
        } catch (RuntimeException e) {
            System.out.println("주문 상품 환불 진행 : 주문 번호 {}"+ orderNumber);
            //String token = refundService.getToken(apiKey, secretKey);
            //refundService.refundWithToken(token, orderNumber, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    @PostMapping("/payment/validation/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        System.out.println("결제 요청 응답. 결제 내역 - 주문 번호: {}" + payment.getResponse().getMerchantUid());
        return payment;
    }
}
