package com.example.ecom.services.jwt.admin.category.FAQ;

import com.example.ecom.dto.FAQDto;

public interface FAQService {
    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
