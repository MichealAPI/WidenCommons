package it.mikeslab.widencommons.api.database.config;

import it.mikeslab.widencommons.WidenCommons;
import it.mikeslab.widencommons.api.database.Database;
import it.mikeslab.widencommons.api.database.SerializableMapConvertible;
import it.mikeslab.widencommons.api.database.SupportedDatabase;
import it.mikeslab.widencommons.api.database.impl.JSONDatabaseImpl;
import it.mikeslab.widencommons.api.database.impl.MongoDatabaseImpl;
import it.mikeslab.widencommons.api.database.impl.SQLDatabaseImpl;
import it.mikeslab.widencommons.api.database.pojo.URIBuilder;
import it.mikeslab.widencommons.api.logger.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ConfigDatabaseUtil<T extends SerializableMapConvertible<T>> {

    private final ConfigurationSection section;
    private final File dataFolder;


    public Database<T> getDatabaseInstance() {

        String typeAsString = section.getString("type");

        SupportedDatabase dbType = validateDatabaseType(typeAsString);

        if(dbType == null) {
            LoggerUtil.log(
                    WidenCommons.PLUGIN_NAME,
                    Level.WARNING,
                    LoggerUtil.LogSource.CONFIG,
                    String.format("Invalid database type: %s", typeAsString)
            );
            return null;
        }

        URIBuilder uriBuilder = composeUriBuilder(section);
        if(uriBuilder == null) {
            LoggerUtil.log(
                    WidenCommons.PLUGIN_NAME,
                    Level.SEVERE,
                    LoggerUtil.LogSource.CONFIG,
                    "An error occurred. Check for previous error messages in console."
            );

            return null;
        }


        Database<T> databaseInstance = null;
        switch (dbType) {

            case SQL -> databaseInstance = new SQLDatabaseImpl<>(uriBuilder);
            case MONGODB -> databaseInstance = new MongoDatabaseImpl<>(uriBuilder);
            case JSON -> databaseInstance = new JSONDatabaseImpl<>(uriBuilder);
        }

        return databaseInstance;

    }


    private URIBuilder composeUriBuilder(ConfigurationSection section) {

        URIBuilder theUriBuilder;
        URIBuilder.URIBuilderBuilder uriBuilderBuilder = URIBuilder.builder();

        String uri = section.getString("uri", null);
        if(uri == null) {
            LoggerUtil.log(
                    WidenCommons.PLUGIN_NAME,
                    Level.SEVERE,
                    LoggerUtil.LogSource.CONFIG,
                    "Database URI is null! Check your config!"
            );
            return null;
        }

        uri = uri.replace("{dataFolder}", dataFolder.getAbsolutePath())
                .replace("/", File.separator)
                .replace("\\", File.separator);

        uriBuilderBuilder.uri(uri);

        // check if it's sqlite
        uriBuilderBuilder.isSqlite(uri.startsWith("jdbc:sqlite"));

        String password = section.getString("password", null);
        String username = section.getString("username", null);
        String table = section.getString("table", null);
        String database = section.getString("database", null);

        if(password != null) uriBuilderBuilder.password(password);
        if(username != null) uriBuilderBuilder.username(username);
        if(database != null) uriBuilderBuilder.database(database);
        if(table != null) uriBuilderBuilder.table(table);

        theUriBuilder = uriBuilderBuilder.build();
        return theUriBuilder;
    }



    private SupportedDatabase validateDatabaseType(String typeAsString) {

        try {
            return SupportedDatabase.valueOf(typeAsString);
        } catch (Exception e) {
            return null;
        }

    }




}
