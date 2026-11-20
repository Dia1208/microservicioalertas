package com.zaror.microservicioalertas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void cuentasDeDemostracionTienenRolesCorrectos() throws Exception {
        mvc.perform(get("/auth/me").header("Authorization", basic("alejandro@encargado.cl")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ENCARGADO"));
        mvc.perform(get("/auth/me").header("Authorization", basic("alejandro@directiva.cl")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("DIRECTIVA"));
    }

    @Test
    void vecinoNoPuedeBorrarYCuentaAnonimaNoPuedeLeer() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"vecino@ejemplo.cl\",\"clave\":\"Vecino2026@\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rol").value("VECINO"));
        mvc.perform(delete("/alertas/1").header("Authorization", basic("vecino@ejemplo.cl", "Vecino2026@")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/alertas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void directivaPuedeAccederAOperacionDeGestion() throws Exception {
        mvc.perform(delete("/alertas/999999")
                        .header("Authorization", basic("alejandro@directiva.cl")))
                .andExpect(status().isNotFound());
    }

    private String basic(String correo) {
        return basic(correo, "Duoc.2026@");
    }

    private String basic(String correo, String clave) {
        String valor = Base64.getEncoder().encodeToString(
                (correo + ":" + clave).getBytes(StandardCharsets.UTF_8));
        return "Basic " + valor;
    }
}
