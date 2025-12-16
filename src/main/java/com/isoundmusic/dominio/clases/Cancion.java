package com.isoundmusic.dominio.clases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cancion {
    private Integer id;
    private String titulo;
    private String artista;
}
