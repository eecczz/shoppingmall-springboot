
package com.example.paymentapi.controller;

import com.example.paymentapi.dto.MemberFormDto;
import com.example.paymentapi.dto.OrderSaveDto;
import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.Member;
import com.example.paymentapi.entity.Order;
import com.example.paymentapi.repository.CartRepository;
import com.example.paymentapi.repository.MemberRepository;
import com.example.paymentapi.repository.OrderRepository;
import com.example.paymentapi.repository.PostRepository;
import com.example.paymentapi.service.CartService;
import com.example.paymentapi.service.OrderService;
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
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

    private final PostService postService;
    private final CartService cartService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PostRepository postRepository;
    private final OrderRepository orderRepository;
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
    public String signupForm(Model model) {
        model.addAttribute("formDto", new MemberFormDto());
        return "/demo/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberFormDto formDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/demo/signup";
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

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestParam("username") String username,
                                                      @RequestParam("password") String password,
                                                      HttpServletRequest request) {
        Optional<Member> memberOpt = memberRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();

        if (memberOpt.isPresent() && Objects.equals(memberOpt.get().getUserpw(), password)) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, memberOpt.get());
            response.put("message", "Login successful");
            response.put("success", true);
            response.put("redirectUrl", "/demo/list");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Invalid username or password");
        response.put("success", false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/demo/list";
    }

    @RestController
    public class DataController {

        @GetMapping("/api/data")
        public Map<String, String> getData() {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Spring Boot와 React 연동 성공!");
            return response;
        }
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listItems(@RequestParam(value = "keyword", required = false) String keyword,
                                                         @RequestParam(value = "pagenum", defaultValue = "0") int pageNum,
                                                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {

        // 검색 조건 설정
        SearchCondition condition = new SearchCondition();
        if (keyword != null) {
            condition.setItemname(keyword);
            condition.setSeller(keyword);
        }

        // 페이지 요청 및 검색 결과 가져오기
        PageRequest pageRequest = PageRequest.of(pageNum, 8);
        Page<PostDto> postPage = postRepository.searchPage(condition, pageRequest);

        // 장바구니 정보 확인
        Cart cart;
        if (sessionMember == null) {
            Member newMember = postService.exMember();
            cart = postService.exCart(newMember);
        } else {
            cart = cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));
        }

        // JSON 데이터를 반환할 맵 구성
        Map<String, Object> response = new HashMap<>();
        response.put("posts", postPage.getContent());
        response.put("totalPages", postPage.getTotalPages());
        response.put("cartCount", cart.getSavedItems().size());

        return ResponseEntity.ok(response);  // JSON 데이터 반환
    }


    @GetMapping("/item-detail/{id}")
    public String itemDetail(@PathVariable("id") Long id, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        PostDto postDto = postService.read(id);
        model.addAttribute("postDto", postDto);

        Cart cart;
        if (sessionMember == null) {
            Member newMember = postService.exMember();
            cart = postService.exCart(newMember);
            model.addAttribute("member", newMember);
        } else {
            cart = cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getSavedItems().size());

        return "/demo/item-detail";
    }

    @ResponseBody
    @PostMapping("/item-post")
    public PostDto addItemToCart(@RequestBody PostDto postDto,
                                 @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart = sessionMember == null ? postService.exCart(postService.exMember()) :
                cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));

        cartService.insert(cart, postService.dtoToEntity(postDto), postDto.getQuantity());
        return postDto;
    }

    @GetMapping("/cart")
    public ResponseEntity<Map<String, Object>> viewCart(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        Member member;
        if (sessionMember == null) {
            member = postService.exMember();
            cart = postService.exCart(member);
        } else {
            member = sessionMember;
            cart = cartRepository.findByMember(member).orElseGet(() -> postService.exCart(member));
        }

        // 장바구니 데이터 및 카트 카운트 반환
        Map<String, Object> response = new HashMap<>();
        response.put("member", member.getNickname());
        response.put("cart", cart.getSavedItems());
        response.put("cartCount", cart.getSavedItems().size());

        return ResponseEntity.ok(response);  // JSON 데이터 반환
    }

    @ResponseBody
    @PostMapping("/deletecart")
    public void removeItemFromCart(@RequestParam("itemId") Long itemId,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart = sessionMember == null ? postService.exCart(postService.exMember()) :
                cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));
        cartService.delete(cart, itemId);
    }

    @ResponseBody
    @PostMapping("/updatecart")
    public void updateCartQuantity(@RequestParam("itemId") Long itemId,
                                   @RequestParam("quantity") Long quantity,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart = sessionMember == null ? postService.exCart(postService.exMember()) :
                cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));
        cartService.update(cart, itemId, quantity);
    }

    @GetMapping("/purchase")
    public String purchase(Model model,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        Cart cart;
        if (sessionMember == null) {
            Member newMember = postService.exMember();
            cart = postService.exCart(newMember);
            model.addAttribute("member", newMember);
        } else {
            cart = cartRepository.findByMember(sessionMember).orElseGet(() -> postService.exCart(sessionMember));
            model.addAttribute("member", sessionMember);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getSavedItems().size());
        return "/demo/purchase";
    }

    @ResponseBody
    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember, @RequestBody List<OrderSaveDto> orderSaveDtos) throws IOException {
        // 주문 번호 가져오기
        String orderNumber = String.valueOf(orderSaveDtos.get(0).getOrderNumber());
        try {
            // 로그인된 사용자가 있을 때
            if (sessionMember != null) {
                // 모든 상품을 구매 처리
                cartService.purchaseAll(sessionMember);
                System.out.println("결제 성공 : 주문 번호 {}" + orderNumber);
                return ResponseEntity.ok("결제가 완료되었습니다. 주문 번호: " + orderNumber);
            }
        } catch (RuntimeException e) {
            // 결제 실패 시 예외 처리 및 환불 진행
            System.out.println("주문 상품 환불 진행 : 주문 번호 {}" + orderNumber);
            // 환불 API 호출 (주석 처리된 부분은 구현 필요)
            // String token = refundService.getToken(apiKey, secretKey);
            // refundService.refundWithToken(token, orderNumber, e.getMessage());
            return new ResponseEntity<>("결제 중 오류 발생: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        // 세션에 사용자가 없을 경우 (비회원 결제 등)
        return new ResponseEntity<>("로그인된 사용자가 없습니다.", HttpStatus.UNAUTHORIZED);
    }
}