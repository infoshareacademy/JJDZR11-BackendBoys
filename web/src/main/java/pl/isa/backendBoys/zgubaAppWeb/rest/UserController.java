package pl.isa.backendBoys.zgubaAppWeb.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.isa.backendBoys.zgubaAppWeb.search.SearchHelp;
import pl.isa.backendBoys.zgubaAppWeb.user.User;
import pl.isa.backendBoys.zgubaAppWeb.user.UserDto;
import pl.isa.backendBoys.zgubaAppWeb.user.UserService;

@Controller
@RequestMapping("user")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginUser(Model model, @ModelAttribute UserDto userDto) {
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute(userDto);
        model.addAttribute("showError", false);
        model.addAttribute("content", "login");
        return "main";
    }

    @PostMapping("/login")
    public String loggedUser(Model model, @ModelAttribute UserDto userDto) {
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute(userDto);
        model.addAttribute(userService);
        if (userService.loginUser(userDto.getLoginEmail(), userDto.getPassword())) {
            model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
            model.addAttribute("content", "loggedIn");
        } else {
            model.addAttribute("showError", true);
            model.addAttribute("content", "login");
        }
        return "main";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("searchWord", new SearchHelp());
        userService.logout();
        model.addAttribute("content", "logout");
        return "main";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("userToAdd", new User());
        model.addAttribute("showError", false);
        model.addAttribute("content", "register");
        return "main";
    }

    @PostMapping("/register")
    public String registerWrongUser(Model model, @ModelAttribute User userToAdd) {
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("userToAdd", new User());
        if (userService.isLoginTaken(userToAdd.getLoginEmail())) {
            model.addAttribute("showError", true);
            model.addAttribute("content", "register");
        } else {
            model.addAttribute("showError", false);
            userService.registerUser(userToAdd);
            model.addAttribute("content", "addedNewUser");
        }
        return "main";
    }

    @GetMapping("/panel")
    public String userPanel(Model model) {
        model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("content", "userPanel");
        return "main";
    }


    @GetMapping("/panel/showUserData")
    public String showUserData(Model model) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("userToModify", loggedUser);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", false);
        model.addAttribute("content", "userPanel_userData");
        return "main";
    }

    @GetMapping("/panel/changeUserData")
    public String getChangeUserData(Model model, @ModelAttribute User userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("userToModify", loggedUser);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", true);
        model.addAttribute("content", "userPanel_userData");
        return "main";
    }

    @PostMapping("/panel/changeUserData")
    public String postChangeUserData(Model model, @ModelAttribute User userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", true);

        if (loggedUser.getPassword().equals(userToModify.getPassword())) {
            userService.changeUserName(loggedUser, userToModify.getName());
            userService.changeUserCity(loggedUser, userToModify.getCity());
            userService.changeUserContactNumber(loggedUser, userToModify.getContactNumber());

            model.addAttribute("userToModify", loggedUser);
            model.addAttribute("content", "userPanel_userData_modified");
        } else {
            model.addAttribute("userToModify", userToModify);
            model.addAttribute("showError", true);
            model.addAttribute("content", "userPanel_userData");
        }
        return "main";
    }


    @GetMapping("/panel/logindata")
    public String showLoginData(Model model) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("userToModify", loggedUser);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", false);
        model.addAttribute("content", "userPanel_loginData");
        return "main";
    }

    @GetMapping("/panel/logindata/change")
    public String changeloginDataGet(Model model) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        UserDto userToModify = new UserDto();
        userToModify.setLoginEmail(loggedUserEmail);
        userToModify.setPassword(loggedUser.getPassword());
        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("userToModify", userToModify);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", true);
        model.addAttribute("content", "userPanel_loginData");
        return "main";
    }

    @PostMapping("/panel/logindata/change")
    public String changeloginDataPost(Model model, @ModelAttribute UserDto userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);

        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("userToModify", userToModify);
        model.addAttribute("modify", true);

        boolean isConfirmPasswordCorrect = userToModify.getCurrentPassword().equals(loggedUser.getPassword());
        if (!isConfirmPasswordCorrect) {
            model.addAttribute("showErrorPassword", true);
            model.addAttribute("loggedUserEmail", loggedUserEmail);
            model.addAttribute("content", "userPanel_loginData");
            return "main";
        }

        boolean isLoginChanged = !userToModify.getLoginEmail().equals(userService.getLoggedUserEmail())
                && userToModify.getLoginEmail() != null;
        boolean isPasswordChanged = !userToModify.getPassword().equals(loggedUser.getPassword())
                && !userToModify.getPassword().equals("");
        if (isLoginChanged) {
            boolean isLoginTaken = userService.isLoginTaken(userToModify.getLoginEmail());
            if (isLoginTaken) {
                model.addAttribute("showErrorLogin", true);
                model.addAttribute("loggedUserEmail", loggedUserEmail);
                model.addAttribute("content", "userPanel_loginData");
                return "main";
            } else {
                userService.changeUserLoginAndRequests(loggedUser, userToModify.getLoginEmail());
                loggedUser.setLoginEmail(userToModify.getLoginEmail());
            }
        }
        if (isPasswordChanged) {
            userService.changeUserPassword(loggedUser, userToModify.getPassword());
        }

        if ((!isLoginChanged && !isPasswordChanged)) {
            model.addAttribute("showErrorNothingChange", true);
            model.addAttribute("loggedUserEmail", loggedUserEmail);
            model.addAttribute("content", "userPanel_loginData");
        } else {
            userService.logout();
            model.addAttribute("content", "userPanel_loginData_modified");
        }
        return "main";
    }


    @GetMapping("/panel/deleteAccount")
    public String deleteAccountConfirmation(Model model, @ModelAttribute UserDto userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("userToModify", loggedUser);
        model.addAttribute("searchWord", new SearchHelp());

        model.addAttribute("loggedUserEmail", loggedUserEmail);
        model.addAttribute("content", "userPanel_deleteAccount");

        return "main";
    }

    @PostMapping("/panel/deleteAccount")
    public String deleteAccount(Model model, @ModelAttribute UserDto userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        User loggedUser = userService.getUserByLogin(loggedUserEmail);
        model.addAttribute("userToModify", loggedUser);
        model.addAttribute("searchWord", new SearchHelp());

        boolean isConfirmPasswordCorrect = userToModify.getPassword().equals(loggedUser.getPassword());
        if (!isConfirmPasswordCorrect) {
            model.addAttribute("showErrorPassword", true);
            model.addAttribute("loggedUserEmail", loggedUserEmail);
            model.addAttribute("content", "userPanel_deleteAccount");
        } else {
            userService.deleteUserAndRequests(loggedUser);
            userService.logout();
            model.addAttribute("content", "userPanel_deleteAccount_notification");
        }
        return "main";
    }

    @GetMapping("/adminpanel")
    public String adminPanel(Model model) {
        model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("content", "adminPanel");
        return "main";
    }

    @GetMapping("/adminpanel/accounts")
    public String manageAccounts(Model model, @ModelAttribute User user) {
        model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("searchWordUser", new SearchHelp());
        model.addAttribute("users", userService.getNotAdminUsers());
        model.addAttribute("content", "adminPanel_users");
        return "main";
    }

    @GetMapping("/adminpanel/accounts/delete/{userLoginEmail}")
    public String deleteAccount(Model model, @PathVariable String userLoginEmail) {
        userService.deleteUserAndRequests(userLoginEmail);

        model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("searchWordUser", new SearchHelp());
        model.addAttribute("users", userService.getNotAdminUsers());
        model.addAttribute("deletedUser", userLoginEmail);

        model.addAttribute("content", "adminPanel_users");
        return "main";
    }

    @GetMapping("/adminpanel/accounts/show/{userLoginEmail}")
    public String adminShowLoginData(Model model, @PathVariable String userLoginEmail) {
        User userToModify = userService.getUserByLogin(userLoginEmail);

        model.addAttribute("loggedUserEmail", userService.getLoggedUserEmail());
        model.addAttribute("userToModify", userToModify);
        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("modify", false);
        model.addAttribute("content", "adminPanel_modifyLoginData");
        return "main";
    }

    @GetMapping("/adminpanel/accounts/modify/{userLoginEmail}")
    public String adminChangeLoginDataGet(Model model, @PathVariable String userLoginEmail,
                                          @ModelAttribute UserDto userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        model.addAttribute("loggedUserEmail", loggedUserEmail);

        userToModify.setLoginEmail(userLoginEmail);
        userToModify.setPassword(userService.getUserByLogin(loggedUserEmail).getPassword());
        //zmienilem z current

        model.addAttribute("userToModify", userToModify);
        model.addAttribute("searchWord", new SearchHelp());

        model.addAttribute("modify", true);
        model.addAttribute("content", "adminPanel_modifyLoginData");
        return "main";
    }

    @PostMapping("/adminpanel/accounts/modify/{userLoginEmail}")
    public String adminChangeLoginDataPost(Model model, @PathVariable String userLoginEmail,
                                           @ModelAttribute UserDto userToModify) {
        String loggedUserEmail = userService.getLoggedUserEmail();
        model.addAttribute("loggedUserEmail", loggedUserEmail);

        model.addAttribute("searchWord", new SearchHelp());
        model.addAttribute("userToModify", userToModify);
        model.addAttribute("modify", true);

        userToModify.setCurrentLoginEmail(userLoginEmail);
        userToModify.setCurrentPassword(userService.getUserByLogin(userLoginEmail).getPassword());

        boolean isLoginChanged = !userToModify.getLoginEmail().equals(userToModify.getCurrentLoginEmail())
                && userToModify.getLoginEmail() != null;
        boolean isPasswordChanged = !userToModify.getPassword().equals(userToModify.getCurrentPassword())
                && !userToModify.getPassword().equals("");
        if (isLoginChanged) {
            boolean isLoginTaken = userService.isLoginTaken(userToModify.getLoginEmail());
            if (isLoginTaken) {
                model.addAttribute("showErrorLogin", true);
                model.addAttribute("content", "userPanel_loginData_modified");
                return "main";
            } else {
                userService.changeUserLoginAndRequests(userLoginEmail, userToModify.getLoginEmail());
            }
        }
        if (isPasswordChanged) {
            if (isLoginChanged) {
                userService.changeUserPassword(userService.getUserByLogin(userToModify.getLoginEmail()), userToModify.getPassword());
            } else {
                userService.changeUserPassword(userService.getUserByLogin(userLoginEmail), userToModify.getPassword());
            }
        }

        if ((!isLoginChanged && !isPasswordChanged)) {
            model.addAttribute("showErrorNothingChange", true);
            model.addAttribute("content", "adminPanel_modifyLoginData");
        } else {
            model.addAttribute("showModifyUserInformation", userToModify.getLoginEmail());
            model.addAttribute("users", userService.getNotAdminUsers());
            model.addAttribute("content", "adminPanel_users");
        }
        return "main";
    }


}
