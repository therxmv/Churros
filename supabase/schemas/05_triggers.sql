-- =============================================================================
-- Triggers
-- =============================================================================

-- ---------------------------------------------------------------------------
-- handle_new_user
-- Fires after every INSERT on auth.users and creates a matching profiles row.
-- Household creation is handled separately during the onboarding flow.
-- ---------------------------------------------------------------------------

create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer
-- Restrict search_path to avoid privilege escalation via schema injection.
set search_path = public
as $$
begin
    insert into public.profiles (id, display_name, avatar_url, push_token)
    values (
        new.id,
        coalesce(new.raw_user_meta_data ->> 'display_name', ''),
        new.raw_user_meta_data ->> 'avatar_url',
        null
    );
    return new;
end;
$$;

-- Drop and recreate so the definition stays in sync with declarative resets.
drop trigger if exists on_auth_user_created on auth.users;

create trigger on_auth_user_created
    after insert on auth.users
    for each row
    execute procedure public.handle_new_user();
