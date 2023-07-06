package com.learning.reactor.productapi.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * @author kuldeep
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    @Id
    private String id;
    private String eventType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEvent that = (ProductEvent) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEventType(), that.getEventType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEventType());
    }

    @Override
    public String toString() {
        return "ProductEvent{" +
                "id='" + id + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
