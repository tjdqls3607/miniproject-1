package com.mycom.myapp.domain.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @GetMapping("/page")
    public String forwardToStaticAdminPage() {
        return "forward:/admin/admin_page.html";  // ✅ 포워딩
    }
}

