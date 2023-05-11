package com.umc.pureum.domain.blame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/blame")
public class BlameController {
    private final BlameService blameService;

}
