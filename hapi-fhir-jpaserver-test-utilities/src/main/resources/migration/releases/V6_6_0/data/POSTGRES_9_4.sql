INSERT INTO HFJ_RESOURCE (
   RES_ID,
   RES_VERSION,
   HAS_TAGS,
   RES_PUBLISHED,
   RES_UPDATED,
   SP_HAS_LINKS,
   HASH_SHA256,
   SP_INDEX_STATUS,
   SP_CMPSTR_UNIQ_PRESENT,
   SP_COORDS_PRESENT,
   SP_DATE_PRESENT,
   SP_NUMBER_PRESENT,
   SP_QUANTITY_NRML_PRESENT,
   SP_QUANTITY_PRESENT,
   SP_STRING_PRESENT,
   SP_TOKEN_PRESENT,
   SP_URI_PRESENT,
   RES_TYPE,
   RES_VER
) VALUES (
   1678,
   'R4',
   false,
   '2023-06-15 09:58:42.92',
   '2023-06-15 09:58:42.92',
   false,
   '6beed652b77f6c65d776e57341a0b5b0596ac9cfb0e8345a5a5cfbfaa59e2b62',
   1,
   false,
   false,
   false,
   false,
   true,
   false,
   false,
   true,
   true,
   'Observation',
   1
);


INSERT INTO HFJ_RES_SEARCH_URL (
   RES_SEARCH_URL,
   CREATED_TIME,
   RES_ID
) VALUES (
   'https://example.com',
   '2023-06-29 10:14:39.69',
   1678
);

INSERT INTO HFJ_REVINFO (
   REV
) VALUES (
   1
);

INSERT INTO MPI_LINK_AUD (
   PID,
   REV,
   REVTYPE,
   PERSON_PID,
   GOLDEN_RESOURCE_PID,
   TARGET_TYPE,
   RULE_COUNT,
   TARGET_PID,
   MATCH_RESULT,
   LINK_SOURCE,
   VERSION,
   EID_MATCH,
   NEW_PERSON,
   SCORE
) VALUES (
   1,
   1,
   0,
   1358,
   1358,
   'PATIENT',
   0,
   1357,
   2,
   0,
   1,
   false,
   true,
   1
);


