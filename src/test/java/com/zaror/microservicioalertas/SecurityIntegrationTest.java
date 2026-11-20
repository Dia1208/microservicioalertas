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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

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
        mvc.perform(get("/fallas").header("Authorization", basic("vecino@ejemplo.cl", "Vecino2026@")))
                .andExpect(status().isOk());
        mvc.perform(get("/comunicados").header("Authorization", basic("vecino@ejemplo.cl", "Vecino2026@")))
                .andExpect(status().isOk());
        mvc.perform(post("/comunicados")
                        .header("Authorization", basic("vecino@ejemplo.cl", "Vecino2026@"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Aviso\",\"mensaje\":\"Intento sin permisos\"}"))
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

    @Test
    void fallasSeCompartenYSoloEncargadoPuedeGestionarlas() throws Exception {
        String falla = "{\"componente\":\"Alumbrado\",\"detalle\":\"Luminaria apagada\",\"prioridad\":\"Alta\"}";
        String creado = mvc.perform(post("/fallas")
                        .header("Authorization", basic("alejandro@encargado.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content(falla))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportadoPor").value("alejandro@encargado.cl"))
                .andReturn().getResponse().getContentAsString();
        long id = new com.fasterxml.jackson.databind.ObjectMapper().readTree(creado).get("id").asLong();
        mvc.perform(get("/fallas").header("Authorization", basic("alejandro@directiva.cl")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Luminaria apagada")));
        mvc.perform(put("/fallas/" + id + "/resuelta")
                        .header("Authorization", basic("alejandro@encargado.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"resuelta\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.resuelta").value(true));
        mvc.perform(post("/fallas").header("Authorization", basic("alejandro@directiva.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content(falla))
                .andExpect(status().isForbidden());
    }

    @Test
    void comunicadosSeCompartenYSoloDirectivaPuedePublicarlos() throws Exception {
        String aviso = "{\"titulo\":\"Reunión\",\"mensaje\":\"Reunión mañana en la sede\"}";
        mvc.perform(post("/comunicados").header("Authorization", basic("alejandro@directiva.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content(aviso))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.autor").value("alejandro@directiva.cl"));
        mvc.perform(get("/comunicados").header("Authorization", basic("alejandro@encargado.cl")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reunión mañana en la sede")));
        mvc.perform(post("/comunicados").header("Authorization", basic("alejandro@encargado.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content(aviso))
                .andExpect(status().isForbidden());
    }

    @Test
    void alertaConFotoSeConservaEnServidor() throws Exception {
        String alerta = "{\"titulo\":\"Robo\",\"descripcion\":\"Robo en la esquina\","
                + "\"tipo\":\"Robo\",\"ubicacion\":\"Sector norte\",\"estado\":\"Activa\","
                + "\"fotoBase64\":\"Zm90bw==\"}";
        mvc.perform(post("/alertas").header("Authorization", basic("alejandro@encargado.cl"))
                        .contentType(MediaType.APPLICATION_JSON).content(alerta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fotoBase64").value("Zm90bw=="))
                .andExpect(jsonPath("$.autor").value("alejandro@encargado.cl"));
        mvc.perform(get("/alertas").header("Authorization", basic("alejandro@directiva.cl")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Zm90bw==")));
    }

    @Test
    void perfilDeVecinoSeRecuperaEnOtroDispositivo() throws Exception {
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"otrovecino@ejemplo.cl\","
                                + "\"clave\":\"Vecino2026@\",\"nombre\":\"Ana Vecina\","
                                + "\"telefono\":\"912345678\",\"direccion\":\"Pasaje Norte\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/auth/me")
                        .header("Authorization", basic("otrovecino@ejemplo.cl", "Vecino2026@")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("VECINO"))
                .andExpect(jsonPath("$.nombre").value("Ana Vecina"))
                .andExpect(jsonPath("$.direccion").value("Pasaje Norte"));
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
