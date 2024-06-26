db.entity_with_tenant.insertMany([
    {
        "_id": ObjectId("6606905a5cf50f20304f4850"),
        "name": "name-update",
        "code": "test_tone",
        "tenant_id": "tone"
    },
    {
        "_id": ObjectId("6606905a5cf50f20304f4840"),
        "name": "tenant-check",
        "code": "test_tone",
        "tenant_id": "tone"
    },
    {
        "_id": ObjectId("66069054c3da2e0c64152d55"),
        "name": "tenant-check",
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
        "name": "test-replace",
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