package com.saifu.saifu_v3.controller

import com.saifu.saifu_v3.model.UserDetails
import com.saifu.saifu_v3.service.MoneyService
import com.saifu.saifu_v3.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController(
        private val userDetails: UserDetails,
        private val userService: UserService,
        private val moneyService: MoneyService
) {

    @GetMapping("/")
    fun index(model: Model): String {
        return if (userDetails.accessToken.isEmpty()) {
            // not login
            "redirect:/login"
        } else {
            val user = userService.getUser()
            val moneyList = moneyService.findAllMoneyList()
            model.addAttribute("user", user)
            "index"
        }

    }

}
