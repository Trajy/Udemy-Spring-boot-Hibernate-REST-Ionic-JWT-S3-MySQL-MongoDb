# __UTILIZANDO APENAS JSONIGNORE__

O tratamento de erros utilizando as `@JsonManagedReference` e `@JsonBackReference`, apresentam problemas ao realizar algumas operacoes, como insercao de dados, para resolver este problema sera utilizado a _annotation_ `@JsonIgnore`, apenas no lado da relacao que nao deseja-se serialzar. 