package org.sguesdon.jsbpdfsplitter.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDPage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    String filename;
    String destination;
    PDPage page;
}
