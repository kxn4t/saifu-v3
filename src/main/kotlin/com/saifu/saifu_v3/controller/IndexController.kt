package com.saifu.saifu_v3.controller

import com.saifu.saifu_v3.form.CalculateForm
import com.saifu.saifu_v3.model.UserDetails
import com.saifu.saifu_v3.service.MoneyService
import com.saifu.saifu_v3.service.UserService
import com.saifu.saifu_v3.utils.Utils
import com.saifu.saifu_v3.utils.toLocalDate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class IndexController(
        private val userDetails: UserDetails,
        private val userService: UserService,
        private val moneyService: MoneyService
) {

    private val FORM_KEY_NAME = "calculateForm"
    private val ERRORS_KEY_NAME = BindingResult.MODEL_KEY_PREFIX + FORM_KEY_NAME

    @GetMapping("/")
    fun index(model: Model): String {
        return if (userDetails.accessToken.isEmpty()) {
            // not login
            "redirect:/login"
        } else {
            val user = userService.getUser()
            model.addAttribute("user", user)

            val form = model.asMap()[FORM_KEY_NAME] ?: CalculateForm()
            model.addAttribute(FORM_KEY_NAME, form)
            "index"
        }
    }

    @PostMapping("/calculate")
    fun calculate(
            @Validated form: CalculateForm,
            bindingResult: BindingResult,
            redirectAttributes: RedirectAttributes): String {
        if (userDetails.accessToken.isEmpty()) {
            // not login
            return "redirect:/login"
        }

        redirectAttributes.addFlashAttribute(FORM_KEY_NAME, form)
        redirectAttributes.addFlashAttribute(ERRORS_KEY_NAME, bindingResult)

        if (bindingResult.hasErrors()) {
            // input error
            return "redirect:/"
        }

        val result = moneyService.calculate(form)
        redirectAttributes.addFlashAttribute("result", result)

        return "redirect:/"
    }

    @PostMapping("/fill-empty-comments")
    fun fillOutEmptyComments(comment: String, from: String, to: String): String {
        val fromDate = from.toLocalDate(Utils.dateTimeFormatter)
        val toDate = to.toLocalDate(Utils.dateTimeFormatter)
        val result = moneyService.fillOutEmptyComments(comment, fromDate, toDate)
        return "redirect:/"
    }

}
