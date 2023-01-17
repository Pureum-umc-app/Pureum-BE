package com.umc.pureum.domain.use;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uses")
public class UseController {
    private final UseProvider useProvider;
    private final UseService useService;
}
