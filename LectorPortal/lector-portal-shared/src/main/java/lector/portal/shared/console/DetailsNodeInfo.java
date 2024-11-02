package lector.portal.shared.console;

import jakarta.persistence.Column;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailsNodeInfo extends NodeInfoAbstract {

    private String desc;

    @Column(length = 400)
    private String png;

    private String title;

}
