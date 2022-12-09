# Test Database Data

База данных эмулируется с помощью докер образа, который запускается
и настраивается в классe IntegrationTestBase c загрузкой схемы БД из файла
src/main/resources/schema.sql. После этого в БД загружаются некоторые тестовые данные,
которые могут быть полезны для проверки данных в тестовых классах, которые находятся в src/test/java/. Деплой и выключение тестового
окружения происходят в фикстурах @BeforeAll и @AfterAll.
Проверка тестовых данных производится матчерами с поддержкой регулярных выражений.
Здесь в тестах проверяются единичные строки, для работы с массивами данных тесты
можно изменить, добавив циклы. А в случае валидации данных - используя параметризованные
тесты с передачей массивов валидных данных через параметры сигнатур тестовых методов.

### 1. Какие проверки на SID можно сделать? Написать сами проверки.
    В тестовом классe SidTest 4 проверки:
    1) на соответствие шаблону регулярного выражения 20 цифр/латинских букв правильного SID
    2) на несоответствие этому шаблону SID с длиной < 20
    3) на несоответствие этому шаблону SID с небуквенными символами 
    4) на выброс исключения БД при SID с длиной > 20

### 2. Провалидировать поле ИНН и вывести некорректные значения. По законодательству ИНН может состоять только из 10 или 12 цифр

    В тестовом классе InnTest 3 проверки:
    1) на соответствие шаблону регулярного выражения только из 10 или 12 цифр правильного INN
    2) на несоответствие этому шаблону INN с длиной 11
    3) на несоответствие этому шаблону INN с нечисловыми символами

### 3. Провалидировать поле ОГРН с учётом того, что данное поле обязательно должно быть заполнено только для российских юридических лиц (у юрлиц ИНН состоит из 10 цифр). Для физлиц данное поле не заполняется (тоже проверить это).

    В тестовом классе OgrnTest комплексная проверка - берутся OGRN для одного
    физлица и одного юрлица, проверяется наличие ОГРН у юрлица и отсутствие его у физлица.
    Также проверяется длина ИНН для обеих лиц.

### 4. Провалидировать столбец SHORT_NAME. Требования к значениям: заглавные буквы (латиница, кирилица),цифры, символы тире и кавычки. Длина значения не более 20 символов.
    
    В тестовом классе ShortNameTest проверяются имена физлица и юрлица на соответствие
    шаблону короткого имени. 

### 5. Провалидировать значение номера телефона PHONE по коду согласно справочнику телефонных кодов. Для России помимо кода +7 допустимо указание кода 8.
  
    Тут сделано упрощенно - проверка соответствия кода 7 или 8 ожидаемому шаблону.
    Если средствами SQL, то можно сравнивать код страны в справочнике с кодом внутри номера телефона

    select COUNTERPARTIES.phone,
    CASE WHEN REGEXP_REPLACE(COUNTERPARTIES.phone, '^8', '+7') like CONCAT ( '_',PHONE_CODE.phn_cd,'%') THEN 'Matched' ELSE 'Not matched' END AS match_status
    from COUNTERPARTIES join PHONE_CODE on
    COUNTERPARTIES.COUNTRY_CD=PHONE_CODE.CNTR_CD

### 6. Какие проверки можно сделать на столбцы FULL_NAME, COUNTRY_CD, ADDRESS ? Написать сами проверки.
    
    ...

### 7. Какие проверки можно сделать на справочники COUNTRIES, PHONE_CODE ? Просто перечислить проверки (без написания скрипта проверки)

    Проверка COUNTRIES на код страны - написана ли она в Upper Case, содержит ли от 2 до 3 символов.
    Для телефонных кодов в PHONE_CODE - длина кода должны быть 1 или 2 символа. 
    Если речь идет о валидности загруженных данных, было бы хорошо сделать параметризованный тест,
    который через параметры сигнатуры тестового метода будет считывать csv файл с актуальными данными 
    о кодах страны и телефонных кодов для сравнения с массивом полученных данных из таблиц.


### 8. Написать запрос, который выводит:
	- список стран (наименование страны)
	- количество контрагентов в этой стране
	Не выводить страны, в которых нет контрагентов.
```
SELECT COUNTRIES.name, COUNT(COUNTERPARTIES.sid)
from COUNTRIES Join COUNTERPARTIES on COUNTRIES.code=COUNTERPARTIES.country_cd
group by COUNTRIES.name
```

### 9. Написать запрос, к таблицам, который выводит следующие данные:
	- ИНН контрагента
	- короткое наименование контрагента
	- номер телефона. Если номер телефона начинается на 8, то изменить на +7. Если номер телефона не заполнен, то выводить значение 0
	- признак иностранного контрагента. Если иностранный контрагент - выводим значение Y, если российский контрагент - выводим значение N

```
SELECT COUNTERPARTIES.inn,
COUNTERPARTIES.short_name,
CASE
    WHEN COUNTERPARTIES.phone is NULL THEN '0'
    WHEN COUNTERPARTIES.phone LIKE '8%' THEN REGEXP_REPLACE(COUNTERPARTIES.phone, '^8', '+7')
    ELSE COUNTERPARTIES.phone
END AS phone,
CASE WHEN COUNTERPARTIES.COUNTRY_CD='RUS' THEN 'N' ELSE 'Y' END AS agent
from COUNTERPARTIES
```
