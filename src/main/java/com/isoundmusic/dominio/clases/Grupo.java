package com.isoundmusic.dominio.clases;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grupo {
    private Integer id;
    private String nombre;
    private List<String> miembros;
}
