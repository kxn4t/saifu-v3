package com.saifu.saifu_v3.controller

import com.saifu.saifu_v3.form.CalculateForm
import com.saifu.saifu_v3.model.UserDetails
import com.saifu.saifu_v3.service.MoneyService
import com.saifu.saifu_v3.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

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
            model.addAttribute("user", user)
            model.addAttribute("calculateForm", CalculateForm())
            "index"
        }
    }

    @PostMapping("/calculate")
    fun calculate(@Validated form: CalculateForm, bindingResult: BindingResult, model: Model): String {
        if (userDetails.accessToken.isEmpty()) {
            // not login
            return "redirect:/login"
        }

        val user = userService.getUser()
        model.addAttribute("user", user)

        if (bindingResult.hasErrors()) {
            // input error
            return "index"
        }
        return "index"
    }

}
