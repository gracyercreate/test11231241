# rankyouressence (Fabric 1.21.11)

Команда: /setrunk <игрок> <bronze|silver|gold|platinum|amethyst|diamond|emerald|ruby>
Иконки: src/main/resources/assets/rankyouressence/textures/font/<rank>.png
(любой размер, в игре они масштабируются до высоты текста).

## Сборка через GitHub
1. Создайте репозиторий на github.com и загрузите ВСЕ файлы из архива (включая папку .github).
2. Каждый push (и кнопка Actions -> build -> Run workflow) запускает сборку.
3. Готовый .jar появится в разделе Releases справа на главной странице репозитория
   (также лежит в Actions -> запуск -> Artifacts).
4. Положите jar в папку mods вместе с Fabric API.
Чтобы заменить иконки: загрузите свои png в src/main/resources/assets/rankyouressence/textures/font/ с теми же именами - сборка запустится сама.
