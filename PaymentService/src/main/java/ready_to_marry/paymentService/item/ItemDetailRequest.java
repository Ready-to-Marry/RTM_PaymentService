package ready_to_marry.paymentService.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import ready_to_marry.paymentService.item.enums.CategoryType;
import ready_to_marry.paymentService.item.enums.FieldType;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDetailRequest {
    private Long itemId;
    private CategoryType category;
    private FieldType field;
    private String name;
    private String region;
    private Long price;
    private String thumbnailUrl;
    private List<String> tags;
    private List<String> styles;

    private String address;
    private String description;
    private String descriptionImageUrl;

    private WeddingHallDetail weddingHallDetail;
    private VideoOrInvitationDetail videoOrInvitationDetail;

    @Getter
    @Builder
    public static class WeddingHallDetail {
        private Integer mealPrice;
        private Integer capacity;
        private Integer parkingCapacity;
    }

    @Getter
    @Builder
    public static class VideoOrInvitationDetail {
        private Integer duration;
    }
}