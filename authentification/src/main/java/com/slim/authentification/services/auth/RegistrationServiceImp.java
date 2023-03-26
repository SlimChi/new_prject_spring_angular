package com.slim.authentification.services.auth;

import com.slim.authentification.config.JwtUtils;
import com.slim.authentification.dto.AuthenticationRequest;
import com.slim.authentification.dto.AuthenticationResponse;
import com.slim.authentification.dto.UserDto;
import com.slim.authentification.models.Role;
import com.slim.authentification.models.User;
import com.slim.authentification.repositories.RoleRepository;
import com.slim.authentification.repositories.UserRepository;
import com.slim.authentification.services.EmailSendService;
import com.slim.authentification.services.RegistrationService;
import com.slim.authentification.services.impl.UserServiceImpl;
import com.slim.authentification.services.token.ConfirmationToken;
import com.slim.authentification.services.token.ConfirmationTokenService;
import com.slim.authentification.validators.ObjectsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author Slimane
 * @Project
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImp implements RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserServiceImpl userServiceImp;
    private final EmailSendService emailSendService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final ObjectsValidator validator;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";


    @Override
    @Transactional
    public AuthenticationResponse registerAdmin(UserDto request) {
        validator.validate(request);

        User user = UserDto.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(
                findOrCreateRole(ROLE_ADMIN)
        );


        var savedUser = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("fullName", savedUser.getFirstName() + " " + savedUser.getLastName());

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(24), user);

        String link = "http://localhost:9090/auth/confirm?token=" + token;
        emailSendService.sendEmail(
                request.getEmail(),
                buildEmail(request.getFirstName(),
                        link));
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    @Override
    @Transactional
    public AuthenticationResponse registerUser(UserDto request) {
       validator.validate(request);

        User user = UserDto.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(
                findOrCreateRole(ROLE_USER)
        );

        var savedUser = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("fullName", savedUser.getFirstName() + " " + savedUser.getLastName());

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(24), user);

        String link = "http://localhost:9090/auth/confirm?token=" + token;
        emailSendService.sendEmail(
                request.getEmail(),
                buildEmail(request.getFirstName(),
                        link));
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }


    private Role findOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElse(null);
        if (role == null) {
            return roleRepository.save(
                    Role.builder()
                            .name(roleName)
                            .build()
            );
        }
        return role;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userServiceImp.enableAppUser(confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;background-color:#FFFFFF\">\n" +
                "     <tbody><tr>\n" +
                "      <td valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                "         <tbody><tr>\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table bgcolor=\"#fad939\" class=\"es-header-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:510px\">\n" +
                "             <tbody><tr>\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0;padding-left:20px;padding-right:20px\">\n" +
                "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tbody><tr>\n" +
                "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:470px\">\n" +
                "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tbody><tr>\n" +
                "                      <td align=\"center\" height=\"40\" style=\"padding:0;Margin:0\"></td>\n" +
                "                     </tr>\n" +
                "                   </tbody></table></td>\n" +
                "                 </tr>\n" +
                "               </tbody></table></td>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "       </tbody></table>\n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tbody><tr>\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:510px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FAD939\">\n" +
                "             <tbody><tr>\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0\">\n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tbody><tr>\n" +
                "                  <td class=\"es-m-p0r\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:510px\">\n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tbody><tr>\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;position:relative\"><img class=\"adapt-img\" src=\"src/main/resources/images/Myprint.png" + "alt=\"\" title=\"\" width=\"510\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "                     </tr>\n" +
                "                   </tbody></table></td>\n" +
                "                 </tr>\n" +
                "               </tbody></table></td>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "       </tbody></table>\n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "         <tbody><tr>\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "           <table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FAD939;border-radius:0 0 50px 50px;width:510px\">\n" +
                "             <tbody><tr>\n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0;padding-left:20px;padding-right:20px\">\n" +
                "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tbody><tr>\n" +
                "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:470px\">\n" +
                "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tbody><tr>\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0\"><h1 style=\"Margin:0;line-height:46px;mso-line-height-rule:exactly;font-family:Poppins, sans-serif;font-size:38px;font-style:normal;font-weight:bold;color:#5d541d\">Please confirm<br>your email address</h1></td>\n" +
                "                     </tr>\n" +
                "                     <tr>\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:40px;padding-bottom:40px\"><h3 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:Poppins, sans-serif;font-size:20px;font-style:normal;font-weight:bold;color:#5D541D\">Hello \"" + name + "\", Thanks for joining MyPrint!</h3><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Poppins, sans-serif;line-height:27px;color:#5D541D;font-size:18px\"><br></p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Poppins, sans-serif;line-height:27px;color:#5D541D;font-size:18px\">To finish signing up, please confirm your email address. This ensures we have the right email in case we need to contact you.</p></td>\n" +
                "                     </tr>\n" +
                "                     <tr>\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0\"><!--[if mso]><a href=\""+ link +"\" target=\"_blank\" hidden>\n" +
                "\t<v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" esdevVmlButton href=\"" + link + "\" \n" +
                "                style=\"height:49px; v-text-anchor:middle; width:254px\" arcsize=\"50%\" stroke=\"f\"  fillcolor=\"#8928c6\">\n" +
                "\t\t<w:anchorlock></w:anchorlock>\n" +
                "\t\t<center style='color:#ffffff; font-family:Poppins, sans-serif; font-size:16px; font-weight:400; line-height:16px;  mso-text-raise:1px'>Confirm email address</center>\n" +
                "\t</v:roundrect></a>\n" +
                "<![endif]--><!--[if !mso]><!-- --><span class=\"msohide es-button-border\" style=\"border-style:solid;border-color:#2CB543;background:#8928c6;border-width:0px;display:inline-block;border-radius:30px;width:auto;mso-hide:all\"><a href=\"" + link +"\" class=\"es-button\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:16px;padding:15px 35px 15px 35px;display:inline-block;background:#8928c6;border-radius:30px;font-family:Poppins, sans-serif;font-weight:normal;font-style:normal;line-height:19px;width:auto;text-align:center;border-color:#8928c6\">Confirm email address</a></span><!--<![endif]--></td>\n" +
                "                     </tr>\n" +
                "                   </tbody></table></td>\n" +
                "                 </tr>\n" +
                "               </tbody></table></td>\n" +
                "             </tr>\n" +
                "             <tr>\n" +
                "              <td align=\"left\" style=\"Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;padding-bottom:40px\">\n" +
                "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                 <tbody><tr>\n" +
                "                  <td align=\"left\" style=\"padding:0;Margin:0;width:470px\">\n" +
                "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "                     <tbody><tr>\n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Poppins, sans-serif;line-height:21px;color:#5D541D;font-size:14px\">Thanks,<br>MyPrint Team!&nbsp;</p></td>\n" +
                "                     </tr>\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi</p></p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +

                "                   </tbody></table></td>\n" +
                "                 </tr>\n" +
                "               </tbody></table></td>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "         </tr>\n" +
                "                 </tr>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "         <tbody><tr>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "       </tbody></table>\n" +
                "         <tbody><tr>\n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\">\n" +
                "             <tbody><tr>\n" +
                "              <td align=\"left\" style=\"padding:20px;Margin:0\">\n" +
                "                 <tbody><tr>\n" +
                "                 </tr>\n" +
                "               </tbody></table></td>\n" +
                "             </tr>\n" +
                "           </tbody></table></td>\n" +
                "         </tr>\n" +
                "       </tbody></table></td>\n" +
                "     </tr>\n" +
                "   </tbody></table>";
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullName", user.getFirstName() + " " + user.getLastName());


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
