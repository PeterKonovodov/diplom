![](logo.png)    
    
# eNotes! v1.6
 
## Краткое описание структуры дипломного проекта по курсу "Введение в Android"    

Данное приложение основано на одной Activity, в которой реализовано    
переключение активных экранов:    
  
 - Экран ввода пин-кода.
 - Экран просмотра и редактирования записей.    
     
  
Переключение экранов выполнено манипуляцией с фрагментами  
**PinFragment** и **NoteListFragment** Об особенностях обработки фрагментов - в конце файла Readme.md    
      
**ThisApp.java** - "стационарный" класс, наследованный от Application.    
В нем инициализируются ключевые сущности, такие как     
noteRepository (хранилище заметок)    
pinStore (хранилище пинкода)    
Там же реализованы некоторые статические утилитарные методы.    
    
**NotesMainActivity.java** - файл единственной Activity приложения.    
Здесь реализована логика переключения экранов, языков, частичное управление вводом пин-кода.    
      
**NotesAdapter.java** - класс, формирующий сортированный список заметок.    
    
Сортировка списка заметок осуществляется четырьмя компараторами в следующем порядке:    
**comparators/IsCompletedComparator.java    
comparators/HasDeadLineComparator.java    
comparators/DeadLineComparator.java    
comparators/ModifyDateComparator.java** 

и делается это в пространстве реализации хранилища заметок    

**SQLiteNoteRepository.java**,  
которое основано на SQLite.    
    
**SharedPrefPinStore.java** хранит пинкод в SharedPreferences в зашифрованном     
с помощью алгоритма AES и преобразованом в текст с помощью Base64 виде.    
    
**Note.java** - класс, описывающий собственно заметку.    
Из особенностей: даты хранятся в виде переменных типа long, т.е. т.н. Epoch time в секундах   
с 01.01.1970.    
Дата дедлайна хранится в секундах, но с обрезанными суточными секундами. Т.е. с точностью до суток.    
Дата модификации - с точностью до секунд.    
    
**NoteEditor.java** - класс-редактор заметки.     
Редактирование заметки основано на AlertDialog и DatePicker.  
  
  
### Об особенностях работы с фрагментами в данном приложении  
Для ввода пин-кода используется фрагмент, объявленный в файле PinFragment.java и использующий   
разметку pin_fragment.xml  
По завершении ввода четырех цифр в различных ситуациях требуется осуществлять различные действия:  
 - проверка введенного пин-кода на корректность для дальнейшей работы  
 - проверка введенного пин-кода на корректность для его сброса  
 - сохранение введенного пин-кода без проверки  
   
Для выполнения различных действий реализован механизм обратных вызовов (callback).   
В классе PinFragment объявлен интерфейс OnPinEntered, реализующий метод onPinEntered(), который  
вызывается в листенере нажатий класса PinFragment после ввода четырех цифр.  
В классе NotesMainActivity в зависимости от требуемых действий фрагменту передаются различные  
методы в качестве методов обратного вызова.  
Эти методы возвращает метод класса NotesMainActivity  

    private PinFragment.OnPinEntered getOnPinEntered(int activityState)  

в зависимости от текущего состояния приложения activityState.  
   
Пересоздание NotesMainActivity при смене ориентации или локализации приложения  
приводит к пересозданию и текущего фрагмента.
При пересоздании фрагмента происходит утеря метода обратного вызова, т.к. пересозданный фрагмент - 
это новый объект, в котором ссылка onPinEntered = null.
Соответственно никаких действий при вводе четырех цифр не происходит.  
  
Возможным способом обойти этот прискорбный факт могло бы стать сохранение ссылки  
на метод обратного вызова в *savedInstanceState* фрагмента с последующим ее  
восстановлением.  
Однако тип Bundle допускает хранение только ограниченного типа данных.   
Второй способ - использование статической ссылки onPinEntered. Но поскольку следует избегать
статических переменных, то для решения этой проблемы были сооружены следующие "костыли":  
  
В onCreate() класса NotesMainActivity выполняется поиск восстановленного фрагмента   
по его тегу (переданному ему при создании):  

     pinFragment = (PinFragment) getSupportFragmentManager().findFragmentByTag(PIN_FRAGMENT_TAG);  

после чего ему передается нужный метод обратного вызова взамен утерянного   
в зависимости от состояния приложения activityState, сохраненного на время переконфигурации  

     if (pinFragment != null) { pinFragment.setOnPinEntered(getOnPinEntered(activityState)); }

Как показали предварительные тесты, это работает.
Если закомментировать эти строки, то можно убедиться, что ввод четырех цифр пин-кода после поворота
экрана или смены языка не приводит ни к какому результату.
