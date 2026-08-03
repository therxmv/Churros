-- =============================================================================
-- storage
-- Supabase Storage bucket definitions and storage RLS policies.
--
-- Buckets
-- -------
--   avatars        — user profile pictures (authenticated read, owner write)
--   family-photos  — household cover photo (member read, parent write)
--
-- NOTE: Storage policies are NOT standard Postgres row-level security.
-- They live in the storage schema and reference storage.objects / storage.buckets.
-- The helper functions from 04_household_members.sql are reused here to avoid
-- raw subqueries against household_members (same self-join concern applies).
-- =============================================================================

-- ---------------------------------------------------------------------------
-- Buckets
-- ---------------------------------------------------------------------------

insert into storage.buckets (id, name, public)
values
    ('avatars',       'avatars',       false),
    ('family-photos', 'family-photos', false)
on conflict (id) do nothing;

-- ---------------------------------------------------------------------------
-- avatars — private bucket
-- Only authenticated users can read avatar objects.
-- Only the owning user can upload, replace, or delete their own avatar.
--
-- Object path convention: avatars/<user_id>/<filename>
-- The owner check relies on the first path segment matching auth.uid().
-- ---------------------------------------------------------------------------

-- Authenticated read — any signed-in user can read avatars.
create policy "avatars: authenticated read"
    on storage.objects
    for select
    to authenticated
    using (bucket_id = 'avatars');

-- Authenticated user can upload (insert) their own avatar.
create policy "avatars: owner can insert"
    on storage.objects
    for insert
    to authenticated
    with check (
        bucket_id = 'avatars'
        and (storage.foldername(name))[1] = auth.uid()::text
    );

-- Authenticated user can replace (update) their own avatar.
create policy "avatars: owner can update"
    on storage.objects
    for update
    to authenticated
    using (
        bucket_id = 'avatars'
        and (storage.foldername(name))[1] = auth.uid()::text
    )
    with check (
        bucket_id = 'avatars'
        and (storage.foldername(name))[1] = auth.uid()::text
    );

-- Authenticated user can delete their own avatar.
create policy "avatars: owner can delete"
    on storage.objects
    for delete
    to authenticated
    using (
        bucket_id = 'avatars'
        and (storage.foldername(name))[1] = auth.uid()::text
    );

-- ---------------------------------------------------------------------------
-- family-photos — private bucket
-- Only verified household members can read the cover photo.
-- Only parents of that household can upload, replace, or delete it.
--
-- Object path convention: family-photos/<household_id>/<filename>
-- The household_id is extracted from the first path segment and passed to the
-- SECURITY DEFINER helpers defined in 04_household_members.sql.
-- ---------------------------------------------------------------------------

-- Any verified household member can read the cover photo.
create policy "family-photos: members can select"
    on storage.objects
    for select
    to authenticated
    using (
        bucket_id = 'family-photos'
        and public.is_household_member((storage.foldername(name))[1]::uuid)
    );

-- Only household parents can upload the cover photo.
create policy "family-photos: parents can insert"
    on storage.objects
    for insert
    to authenticated
    with check (
        bucket_id = 'family-photos'
        and public.is_household_parent((storage.foldername(name))[1]::uuid)
    );

-- Only household parents can replace the cover photo.
create policy "family-photos: parents can update"
    on storage.objects
    for update
    to authenticated
    using (
        bucket_id = 'family-photos'
        and public.is_household_parent((storage.foldername(name))[1]::uuid)
    )
    with check (
        bucket_id = 'family-photos'
        and public.is_household_parent((storage.foldername(name))[1]::uuid)
    );

-- Only household parents can delete the cover photo.
create policy "family-photos: parents can delete"
    on storage.objects
    for delete
    to authenticated
    using (
        bucket_id = 'family-photos'
        and public.is_household_parent((storage.foldername(name))[1]::uuid)
    );
