package com.stad.pricing.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Marca esta clase como un controlador Spring MVC.
 * Se utiliza para manejar peticiones HTTP desde el navegador o un cliente.
 */
@Controller
public class IndexRedirectController {

 /**
  * Mapea las peticiones HTTP GET a la raíz "/" del servidor.
  * Es útil cuando el usuario accede directamente al dominio.
  */
 @GetMapping("/")
 public String redirectToIndex() {
     // Redirige la solicitud a /index.html (ubicado en src/main/resources/static/index.html).
     // Spring lo interpreta como una redirección HTTP 302 (Found).
     return "redirect:/index.html";
 }
}