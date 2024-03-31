db.entity_with_tenant.insertMany([
    {
        "_id": ObjectId("6606905a5cf50f20304f4850"),
        "name": "name-updated",
        "code": "test_tone",
        "tenant_id": "tone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d55"),
        "name": "test-1711706196",
        "code": "test_ttwo",
        "tenant_id": "ttwo"
    },

    {
        "_id": ObjectId("66069054c3da2e0c64152d58"),
        "name": "test-replace",
        "code": "test_ttwo",
        "tenant_id": "ttwo"
    },

    {
        "_id": ObjectId("66069054c3da2e0c64152d57"),
        "name": "replaced-entity",
        "code": "test_tone",
        "tenant_id": "tone",

    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d10"),
        "name": "test-remove",
        "code": "test_tone",
        "tenant_id": "tone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d11"),
        "name": "test-remove",
        "code": "test_ttwo",
        "tenant_id": "ttwo"
    }
]);