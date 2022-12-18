# **Gestor de citas para una clinica de fisioterapia**
Esta aplicación trata sobre la gestión de los datos de una fisioterapia, la base de datos tendrá las sigiuentes tablas:

|Tabla| Nombre              | Descripción |
|--   |--                   |--           |
|01   |Client               | Contiene la información sobre los clientes de la clinica |
|02   |Appointment          | Contiene la información sobre las citas de los clientes |
|03   |Treated_area         | Cada cita tendrá las areas de cuerpo que se trarán en ella |
|04   |Tool                 | Las herramientas de las que dispone la clinica |
|05   |appointment_use_tool | Las herramientas que se han usado en cada cita| 

## **Relación de las tablas**
* Client
    * **_(1-N)_** Appointment
* Appointment
    * **_(1-1)_** Treated_area
    * **_(N-N)_** Tool
* appointment_use_tool
    * Debido a la relación anterior **N:N**, se creará esta tabla, que une la tabla **Appointment** con la tabla **Tool** 

## **Como ejecutar el script de mysql en la terminal**
1. Abrir **cmd** y ejecutar **mysql**
2. Acceder al usuario propio de mysql
3. Escribir **source** y el **path** del script 
    * _source my.sql_
    * _source C:\Users\Usuario\Documents\my.sql_ (si se encuentra en otra ruta)
4. Revisa que se haya creado la base de datos **physio** y las tablas

## **Como utilizar el programa**
* Para ejecutar el programa no hace falta ningun argumento
* La interfaz de comandos muestra al usuario las opciones disponibles
* El usuario debe introducir el numero de la opción que desea ejecutar
* Cada consulta ejecutada de tipo **_SELECT_** que devuelva un resultado, se almacenará en una variable, el cual podremos **exportar** en _.XML_ cuando nosotros le indiquemos
* Los archivos **XML** se escribirán en la carpeta **output** por defecto en la ubicación donde esté situada la terminal de comandos, si no existe, se crea automaticamente. Esta carpeta se puede cambiar por otra en el programa
    * _C:/ubicacion/de/la/terminal/output_
* Los archivos **JSON** se leerán desde la carpeta **input** por defecto en la ubicación donde esté situada la terminal de comandos, si no existe, se crea automaticamente. Esta carpeta se puede cambiar por otra en el programa
    * _C:/ubicacion/de/la/terminal/input_
* La salida del programa se mostrará en la consola, el cual deberá ser compatible con la lectura de **colores ASCII**, estas terminales pueden ser la de **Linux** o **GIT Bash**, entre algunos otros. **_No es compatible con la terminal de Windows_**

## **Ejemplos de formatos JSON aceptado por el programa**
* **Client**
```
{
  "clients": [
    {
      "dni": "78295066F",
      "name": "Juan Perez",
      "surname": "Perez",
      "phone": "123456789",
      "address": "Av. Siempreviva 742",
      "city": "Springfield",
      "birthDate": "1990-01-01"
    }
  ]
}
```
* **Appointment** -> Todas las areas tratadas, como cervical y dorsal, se parsearán como **True** cuando se lea el archivo JSON, el resto de campos de la tabla **treated_area** que no aparecen en el fichero se guardan como **False**
```
{
  "appointments": [
    {
      "client_dni": "78295066F",
      "date": "2018-11-01",
      "time": "12:20:00",
      "duration": 35,
      "treatedArea": {
        "areas": [
          "cervical",
          "dorsal",
          "sacroiliac",
          "wrist",
          "hip",
          "foot"
        ],
        "observations": "campo opcional, se puede dejar en blanco"
      }
    }
  ]
}
```
* **Tool**
```
{
  "tools": [
    {
      "name": "my costumized tool",
      "stock": 10
    }
  ]
}
```