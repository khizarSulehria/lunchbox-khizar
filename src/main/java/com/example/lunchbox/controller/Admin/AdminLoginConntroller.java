package com.example.lunchbox.controller.admin;

import com.example.lunchbox.model.entity.Admin;
import com.example.lunchbox.model.entity.Foodmaker;
import com.example.lunchbox.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController()
@RequestMapping(value = "/admin")
public class AdminLoginConntroller {

    private AdminService adminService;

    @Autowired
    public AdminLoginConntroller(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@RequestParam String adminEmail, @RequestParam String adminPassword,
                              HttpSession session, Model model) {

        Admin admin = adminService.login(adminEmail, adminPassword);

        if(admin == null) {
            model.addAttribute("loginError", "Error logging in. Please try again");
            return "login";
        }

        session.setAttribute("loggedinAdmin", admin);
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RedirectView logout(@ModelAttribute Admin admin, HttpSession session) {
        session.removeAttribute("loggedinAdmin");
        return new RedirectView("/");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestBody Admin admin) {
        if(admin.getAdminPassword() != null && admin.getAdminEmail() != null && admin.getAdminName() != null &&
                admin.getAdminNic() != null &&admin.getAdminPhoneNumber() != null)
        {
            adminService.adminSignup(admin);
            return "admin added";
        }
        return "please specify the fields";
    }

    @RequestMapping(value = "/update-admin", method = RequestMethod.POST)
    public String updateAdmin(@RequestBody Admin admin) {
        if(admin.getAdminPassword() != null && admin.getAdminEmail() != null && admin.getAdminName() != null &&
                admin.getAdminNic() != null &&admin.getAdminPhoneNumber() != null)
        {
            adminService.updateAdmin(admin);
            return "admin updated";
        }
        return "please specify the fields";
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.POST)
    public String updatePassword(@RequestParam String oldpassword, @RequestParam String newpassword, @RequestParam String adminEmail) {
        if (adminService.updatePassword(oldpassword, newpassword, adminEmail)) {
            return "password updated";
        }
        return "error";
    }

    @RequestMapping(value = "/count-admins", method = RequestMethod.GET)
    public long countAdmins() {
        return adminService.countAllAdmins();
    }

    @RequestMapping(value = "/admins-list", method = RequestMethod.GET)
    public List<Admin> findAllAdmins() {
        return adminService.findAllAdmin();
    }

    @RequestMapping(value = "/delete-admin", method = RequestMethod.POST)
    public String deleteAdmin(@RequestParam String adminEmail) {
        adminService.deleteAdmin(adminEmail);
        return "admin deleted";
    }

    @RequestMapping(value = "/search-admin", method = RequestMethod.POST)
    public List<Admin> searchAdmins(@RequestParam String adminName) {
        return adminService.getAdminByname(adminName);

    }

    @RequestMapping(value = "/delete-admin-id", method = RequestMethod.POST)
    public void deleteAdminThroughId(@RequestParam Integer adminId){
        adminService.deleteAdminById(adminId);
    }


    @RequestMapping(value = "/admin-listing" , method = RequestMethod.GET)
    public ModelAndView adminDetail() {
        ModelAndView modelAndView = new ModelAndView("admin-listing");
        return modelAndView;
    }

    @RequestMapping(value = "/add-admin" ,method = RequestMethod.GET)
    public ModelAndView getAddAdminView(){
        ModelAndView modelAndView = new ModelAndView("add-admin");
        return modelAndView;
    }

    @RequestMapping(value = "/add-admin" ,method = RequestMethod.POST)
    public ModelAndView getOrderDetail(@RequestParam Integer rowId){
        Admin admin = adminService.getAdminById(rowId);
        ModelAndView modelAndView = new ModelAndView("add-admin");
        modelAndView.addObject("foodmakerDetail", admin);

        return modelAndView;
    }
}
