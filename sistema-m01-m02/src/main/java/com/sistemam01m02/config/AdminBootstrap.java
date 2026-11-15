package com.sistemam01m02.config;

import com.sistemam01m02.identity.dto.Contracts.Register;
import com.sistemam01m02.identity.repository.UserRepository;
import com.sistemam01m02.identity.service.IdentityService;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Alta inicial optativa y sin credenciales predeterminadas. Nunca promueve cuentas existentes. */
@Component
@ConditionalOnProperty(name = "app.bootstrap.enabled", havingValue = "true")
public class AdminBootstrap implements ApplicationRunner {
  private final IdentityService identity;
  private final Environment env;
  private final UserRepository users;
  private final Validator validator;

  public AdminBootstrap(IdentityService s, Environment e, UserRepository u, Validator v) {
    identity = s;
    env = e;
    users = u;
    validator = v;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    // Mantener el bootstrap habilitado es seguro: solo crea al admin en una BD vacía.
    if (users.count() != 0) return;
    Register input =
        new Register(
            env.getRequiredProperty("app.bootstrap.email"),
            env.getRequiredProperty("app.bootstrap.password"),
            "Administrador");
    if (!validator.validate(input).isEmpty())
      throw new IllegalStateException("Credenciales bootstrap inválidas");
    var user = identity.register(input);
    identity.assignRoles(user.id(), Set.of("ADMIN"));
  }
}
