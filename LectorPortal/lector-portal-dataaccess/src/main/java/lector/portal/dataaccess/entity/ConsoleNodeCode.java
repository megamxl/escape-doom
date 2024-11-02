package lector.portal.dataaccess.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lector.portal.shared.model.CodingLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ConsoleNodeCode {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private CodingLanguage language;

    @Column(length = 400)
    private String functionSignature;

    @Column(length = 1000)
    private String input;

    private String variableName;

    private String expectedOutput;

}
