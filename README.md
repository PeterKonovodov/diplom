![](logo.png)  
  
# eNotes!  
## Краткое описание структуры дипломного проекта по курсу "Введение в Android"  
  
Данное приложение основано на одной Activity, в которой реализовано  
переключение активных экранов:  

 - Основной экран - просмотр и редактирование записей.  
 - Экран ввода пин-кода.  

Переключение экранов выполнено манипуляцией с фрагментами
**PinFragment** и **NoteListFragment**  
  
**ThisApp.java**  - "стационарный" класс, наследованный от Application.  
В нем инициализируются ключевые сущности, такие как   
noteRepository (хранилище заметок)  
pinStore (хранилище пинкода)  
Там же реализованы некоторые статические утилитарные методы.  
  
**NotesMainActivity.java** - файл единственной Activity приложения.  
Здесь реализована логика переключения экранов, языков, частичное управление вводом пин-кода.  
  
**PinManager.java**  - класс, реализующий ввод пин-кода и его обработку  
    
**NotesAdapter.java** - класс, формирующий сортированный список заметок.  
  
Сортировка списка заметок осуществляется четырьмя компараторами в следующем порядке:  
**comparators/IsCompletedComparator.java  
comparators/HasDeadLineComparator.java  
comparators/DeadLineComparator.java  
comparators/ModifyDateComparator.java**  
и делается это в пространстве реализации хранилища заметок  
**SQLiteNoteRepository.java**,  которое основано на SQLite.  
  
**SharedPrefPinStore.java** хранит пинкод в SharedPreferences в зашифрованном   
с помощью алгоритма AES и преобразованом в текст с помощью Base64 виде.  
  
**Note.java** - класс, описывающий собственно заметку.  
Из особенностей: даты хранятся в виде переменных типа long, т.е. т.н. Epoch time в секундах с 01.01.1970.  
Дата дедлайна хранится в секундах, но с обрезанными суточными секундами. Т.е. с точностью до суток.  
Дата модификации - с точностью до секунд.  
  
**NoteEditor** - класс-редактор заметки.   
Редактирование заметки основано на AlertDialog и DatePicker.