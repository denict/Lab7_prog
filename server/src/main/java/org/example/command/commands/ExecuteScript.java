package org.example.command.commands;

import org.example.command.Command;
import org.example.entity.Organization;
import org.example.entity.builders.OrganizationBuilder;
import org.example.managers.CommandManager;
import org.example.managers.RunnerScriptManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.ConsoleInput;
import org.example.utility.ConsoleOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Команда "execute_script".
 * Описание команды: считать и исполнить скрипт из указанного файла.
 */
public class ExecuteScript extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 57L;
    private final CommandManager commandManager;
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final RunnerScriptManager runnerScriptManager;

    public ExecuteScript(CommandManager commandManager, ConsoleInput consoleInput, ConsoleOutput consoleOutput, RunnerScriptManager runnerScriptManager) {
        super("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", 1, "\"file_script\"");
        this.commandManager = commandManager;
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.runnerScriptManager = runnerScriptManager;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    public Response execute(Request request) {
        String pathName;
        if (request.getArgs() instanceof String) {
            pathName = (String) request.getArgs();
        }
        else {
            String[] arguments = (String[]) request.getArgs();

            pathName = arguments[0];
        }
        StringBuilder sb = new StringBuilder("");
        try {
            if (pathName == null) {
                return new Response(false, "Конец рекурсии или конец последнего вызова");
            }
            File scriptFile = new File(pathName);
//            Scanner scanner = new Scanner(scriptFile);
            if (!scriptFile.exists()) {
                return new Response(false, "Файл \"" + pathName + "\" не найден");
            }

            if (RunnerScriptManager.checkIfFileInStack(pathName)) {
                sb.append("Файл \"" + pathName + "\" вызывается рекурсивно. Уберите рекурсию исполняемых файлов!"+"\n");
                return new Response(false, sb.toString());
            }
            else {
                RunnerScriptManager.addFile(scriptFile.getName());
            }

            sb.append("Начало исполнения файла \"" + pathName + "\"\n");

            ConsoleInput.setFileMode(true);


            while (runnerScriptManager.hasNextLine()) {
                String line = runnerScriptManager.readLine();
                String[] requestArray = line.split(" ");
                String nameCommand = requestArray[0].toLowerCase();
                String[] args = Arrays.copyOfRange(requestArray, 1, requestArray.length);
                if(commandManager.getCommands().containsKey(nameCommand))
                    if (nameCommand.equals("execute_script")) {
                        ExecuteScript innerScript = new ExecuteScript(commandManager, consoleInput, consoleOutput, new RunnerScriptManager());
                        Response innerResponse = innerScript.execute(new Request(innerScript, args[0]));
                        sb.append(innerResponse.getMessage() + "\n\n");
//                        sb.append(commandManager.getCommands().get(nameCommand).execute(new Request(new ExecuteScript(commandManager, consoleInput, consoleOutput, new RunnerScriptManager()), args[0])).getMessage() + "\n\n");
                    } else if (nameCommand.equals("add") || nameCommand.equals("add_if_min")) { // С добавлением объекта коллекции
                        Organization organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
                        sb.append(commandManager.getCommands().get(nameCommand).execute(new Request(organization, commandManager.getCommands().get(nameCommand), request.getUser())).getMessage() + "\n\n");
                    } else if (nameCommand.equals("update_by_id")) { // объект коллекции + аргумент
                        Organization organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
                        sb.append(commandManager.getCommands().get(nameCommand).execute(new Request(organization, commandManager.getCommands().get(nameCommand), args, request.getUser())).getMessage() + "\n\n");
                    } else if (nameCommand.equals("count_by_official_address") || nameCommand.equals("remove_by_id") // с 1 или 2 аргументами
                            || nameCommand.equals("filter_by_annual_turnover")) {
                        sb.append(commandManager.getCommands().get(nameCommand).execute(new Request(commandManager.getCommands().get(nameCommand), args, request.getUser())).getMessage() + "\n\n");
                    } else { // без аргументов
                        sb.append(commandManager.getCommands().get(nameCommand).execute(new Request(commandManager.getCommands().get(nameCommand), request.getUser())).getMessage() + "\n\n");}

                if (!runnerScriptManager.hasNextLine()) {
                    break;
                }
            }

            sb.append("Завершение исполнения файла \"" + pathName + "\"\n");

            RunnerScriptManager.removeFile(pathName);
            ConsoleInput.setFileMode(false);
            return new Response(true, sb.toString());
        } catch (FileNotFoundException e) {
            return new Response(false, "Файл \"" + pathName + "\" не найден");
        }
    }
}

//                RunnerManager.runCommand(line.split(" "), commandManager, consoleOutput);
