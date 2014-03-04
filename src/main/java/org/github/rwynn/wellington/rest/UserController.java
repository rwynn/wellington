package org.github.rwynn.wellington.rest;

import org.github.rwynn.wellington.exception.ValidationException;
import org.github.rwynn.wellington.services.UserService;
import org.github.rwynn.wellington.transfer.FilterDTO;
import org.github.rwynn.wellington.transfer.UserDTO;
import org.github.rwynn.wellington.utils.SecurityContextUtils;
import org.github.rwynn.wellington.validation.NewUser;
import org.github.rwynn.wellington.validation.UpdateLock;
import org.github.rwynn.wellington.validation.UpdateRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping(value = "/users")
@ResponseBody
public class UserController extends RESTErrorHandler {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RESTResult<UserDTO> register(@Validated(NewUser.class) @RequestBody UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        } else {
            UserDTO created = userService.saveUser(userDTO);
            return new RESTResult<UserDTO>(created);
        }
    }

    @RequestMapping(value = "/admin/lock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RESTResult<UserDTO> updateLock(@Validated(UpdateLock.class) @RequestBody UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        } else {
            UserDTO updated = userService.updateLock(userDTO);
            return new RESTResult<UserDTO>(updated);
        }
    }

    @RequestMapping(value = "/admin/roles", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RESTResult<UserDTO> updateRoles(@Validated(UpdateRoles.class) @RequestBody UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        } else {
            UserDTO updated = userService.updateRoles(userDTO);
            return new RESTResult<UserDTO>(updated);
        }
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public RESTResult<UserDTO> auth() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(SecurityContextUtils.getUsername());
        userDTO.setAuthorities(SecurityContextUtils.getAuthorities());
        return new RESTResult<UserDTO>(userDTO);
    }

    @RequestMapping(value = "/admin/list", method = RequestMethod.GET)
    public RESTResult<RESTPage<UserDTO>> list(@PageableDefault(size = 1, sort = "username") Pageable pageable,
                                              FilterDTO filterDTO) {
        return new RESTResult<RESTPage<UserDTO>>(userService.getUsers(filterDTO, pageable));
    }

    @RequestMapping(value = "/admin/listAdmin", method = RequestMethod.GET)
    public RESTResult<RESTPage<UserDTO>> listAdmin(@PageableDefault(size = 1, sort = "username") Pageable pageable) {
        return new RESTResult<RESTPage<UserDTO>>(userService.getAdmins(pageable));
    }

}
