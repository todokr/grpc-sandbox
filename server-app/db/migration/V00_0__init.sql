create table todos (
    id text primary key,
    title text not null,
    description text,
    progress text not null
);

create index todos_progress_index on todos (progress);