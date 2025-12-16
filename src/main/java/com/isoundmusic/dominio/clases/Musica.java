package com.isoundmusic.dominio.clases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Musica {
    private String id;
    private String titulo;
    private String artista;
}
