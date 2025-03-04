package pyler.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyler.contents.RequestMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMap.api + RequestMap.v1 + RequestMap.image)
public class ImageHubController {


}
