package com.example.AgriConnect.Controller;
import com.example.AgriConnect.Exception.AnyException;
import com.example.AgriConnect.Model.UserDetails1;
import com.example.AgriConnect.Model.UserPrinciples;
import com.example.AgriConnect.Model.UserRegistrationDto;
import com.example.AgriConnect.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {
    public  static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @GetMapping("/Agriconnect.com")
    public String Homepage()
    {
        return "Agriconnect";
    }
    @GetMapping("/Agriconnect")
    public String Homepage1()
    {
        return "Agriconnect";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @GetMapping("/terms")
    public String terms()
    {
        return "terms";
    }
    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }
    @GetMapping("/Option")
        public String showOptionsPage(Model model, HttpSession session ,@AuthenticationPrincipal UserPrinciples userPrinciples) {
            String username= userPrinciples.getUsername();
            if(username==null)
            {

                    log.error("Username not found for the current user. Possible authentication issue.");
                     throw new AnyException("Sorry, we couldn't retrieve your username. Please log in again or contact support if the issue persists.");
                }
            try
            {
                UserDetails1 userDetails1=userService.findByUsername(username);
                if (userDetails1 == null) {
                    log.error("UserDetails not found for username {}. This may be due to incorrect username or database issues.", username);
                    throw new AnyException("Sorry, we couldn't find your user details. Please verify your username or try logging in again. If the problem persists, contact support.");
                }
                session.setAttribute("UserId",userDetails1.getUserId());
            }
            catch (Exception e) {
                log.error("An internal error occurred while setting the UserId in the session for username {}. Exception: {}", username, e.getMessage());
                throw new AnyException("Sorry, an unexpected error occurred while setting up your session. Please try logging in again. If the problem persists, contact support.");
            }
            Long userId = (Long) session.getAttribute("UserId");
            log.info("UserId:{}", userId);
            model.addAttribute("UserId", userId); // Add UserId to model for dynamic links
            return "Option"; // Thymeleaf template for options page
    }
    // Register a new user
    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDto registrationDto, Model model ) {
        //Register The User  with Provided Details By The user and save It In The Database
        UserDetails1 userDetails1=userService.register(registrationDto);

        /*
           Set The UserId in the session And Send To CropPrice and Crop recommendation Controller
           To set The Userid In The in Both Entity in The DataBase To Build The RelationShip
           To Get The Historical data of Their own To Not Others For Data Privacy

         */
        model.addAttribute("message", "User Registered Successfully");
        return "sucessfullyregister";  // Redirect to a success page or back to the registration page
    }
        // Update user details
    @PutMapping("/UpdateUser/{UserId}")
    public ResponseEntity<String> updateUser(@PathVariable Long UserId, @RequestBody UserDetails1 userDetails) {
        UserDetails1 updatedUserDetails = userService.updateUser(UserId, userDetails);
        if (updatedUserDetails == null) {
            return new ResponseEntity<>("Failed to Update the User", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User Details Updated Successfully", HttpStatus.OK);
    }
    @GetMapping("/logout-success")
    public String Logout(HttpServletRequest request ,Model model)
    {
        HttpSession session=request.getSession(false);
        if(session!=null)
        {
            session.invalidate();
        }
        model.addAttribute("secondsLeft", 20);
        request.getCookies();
        return "logout";
    }
    @GetMapping("/AgriconnectChatBot")
    public  String chatBoat()
    {
        return "AgriconnectChatBot";
    }
}