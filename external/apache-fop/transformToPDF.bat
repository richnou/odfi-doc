echo off


set oname=%3%
set odest=%2%
set input=%1%

@echo Transforming: %input% 
@echo Destination: %odest%\%oname%
@echo Destination Name: %oname%
@echo Script path: %~dp0%

set fcmd=%~dp0%\fop-trunk\fop
set rcmd=%fcmd% -fo "%1%" -pdf "%oname%"

@echo FOP Command: %fcmd% -fo %input% -pdf %odest%\%oname%
@echo Res Command: %rcmd%

%fcmd% -fo %input% -pdf %odest%\%oname%