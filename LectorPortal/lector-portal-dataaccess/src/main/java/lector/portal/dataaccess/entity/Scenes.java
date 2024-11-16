package lector.portal.dataaccess.entity;

import lector.portal.shared.model.Node;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scenes {

    private Long id;

    private String name;

    private String bgImg;

    private List<Node> nodes;
}