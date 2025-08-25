-- Delete all orders with HYDROGEN_FRIENDS contract type
DELETE
FROM "order"
WHERE contract_type = 'HYDROGEN_FRIENDS';

-- Remove HYDROGEN_FRIENDS from the contract_type enum
-- First, we need to create a new enum without HYDROGEN_FRIENDS
CREATE TYPE contract_type_new AS ENUM (
    'CONTRACTED_OWNER',
    'GREEN_ALCOHOL_PIONEER',
    'GREEN_ALCOHOL_PARTNERS'
    );

-- Update the order table to use the new enum type
ALTER TABLE "order"
    ALTER COLUMN contract_type TYPE contract_type_new
        USING contract_type::TEXT::contract_type_new;

-- Update the user table to use the new enum type
ALTER TABLE "user"
    ALTER COLUMN current_contract_type TYPE contract_type_new
        USING current_contract_type::TEXT::contract_type_new;

-- Drop the old enum
DROP TYPE contract_type;

-- Rename the new enum to the original name
ALTER TYPE contract_type_new RENAME TO contract_type;