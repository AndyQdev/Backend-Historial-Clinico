package app.backendclinic.atencioncitas.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String userId;
    private String title;
    private String content;

    // Getters y Setters
}
