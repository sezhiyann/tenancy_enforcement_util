db.entity_with_tenant_and_context.insertMany([
    {
        "_id": ObjectId("6606905a5cf50f20304f4850"),
        "name": "name-4-update",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_aone"
    },
    {
        "_id": ObjectId("6606905a5cf50f20304f4840"),
        "name": "tenant-check",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_aone"
    },
    {
        "_id": ObjectId("6606905a5cf50f20304f4841"),
        "name": "tenant-check",
        "code": "test_atwo",
        "tenant_id": "tone",
        "additional_context_id": "tone_atwo"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d55"),
        "name": "tenant-check",
        "code": "test_ttwo",
        "tenant_id": "ttwo",
        "additional_context_id": "ttwo_aone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d58"),
        "name": "test-replace",
        "code": "test_ttwo",
        "tenant_id": "ttwo",
        "additional_context_id": "ttwo_aone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d57"),
        "name": "entity-4-replace",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_aone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d67"),
        "name": "entity-4-replace",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_atwo"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d10"),
        "name": "test-remove",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_aone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d19"),
        "name": "test-remove",
        "code": "test_tone",
        "tenant_id": "tone",
        "additional_context_id": "tone_atwo"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d11"),
        "name": "test-remove",
        "code": "test_ttwo",
        "tenant_id": "ttwo",
        "additional_context_id": "ttwo_aone"
    }
]);