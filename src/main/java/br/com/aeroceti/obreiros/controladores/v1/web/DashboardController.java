/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para a apresentacao do Dashboard do sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
public class DashboardController {

//    private SessionRegistry sreg ;
//    private I18nService i18svc;
    
    Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @GetMapping("/")
    public String getHomePage(/*Authentication authentication, */ Model model){
        model.addAttribute("name", "Murilo");
        logger.info("Redirecionando view para Pagina Inicial...");
        return "redirect:/dashboard";        
    }   
    
    @GetMapping("/login")
    public String getLoginPage(){
        logger.info("Redirecionando view para pagina de Login...");
        return "login";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model){
        model.addAttribute("pageTitle", "Dashboard");
        logger.info("Redirecionando para o Dashboard...");
        return "dashboard";
    }

    @GetMapping("/403erro")
    public String getErrorPage403(){
        logger.info("Redirecionando view para Pagina de Erro 403...");
        return "Error403";
    }
    
    @GetMapping("/api-doc")
    public void redirectSwagger(HttpServletRequest request, HttpServletResponse response) {
        String url = "/swagger-ui/index.html";
        response.setHeader("Location",url);
        response.setStatus(302);
    }
       
}
/*                    End of Class                                            */