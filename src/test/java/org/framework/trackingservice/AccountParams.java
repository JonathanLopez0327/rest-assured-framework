package org.framework.trackingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountParams {
    private String accountName;
    private String accountDescription;
    private String accountType;
    private long totalAmount;
}
