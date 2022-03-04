/* Location: jar://C:/Users/Henry/Documents/cabbagescape/.gradle/loom-cache/1.18.2/net.fabricmc.yarn.1_18_2.1.18.2+build.1-v2/minecraft-project-@-merged-named.jar!/net/minecraft/command/argument/MessageArgumentType.class
 * Java language version: 17
 * Class File: 61.0
 * JD-Core Version: 1.1.3
 */

package com.cabbagegod.cabbagescape.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ClientMessageArgumentType
        implements ArgumentType<ClientMessageArgumentType.MessageFormat>
{
    private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Hello world!", "foo", "@e", "Hello @p :)" });

    public static ClientMessageArgumentType message() {
        return new ClientMessageArgumentType();
    }

    public static Text getMessage(CommandContext<FabricClientCommandSource> command, String name) throws CommandSyntaxException {
        return ((MessageFormat)command.getArgument(name, MessageFormat.class)).format(command.getSource(), true);
    }


    public MessageFormat parse(StringReader stringReader) throws CommandSyntaxException {
        return MessageFormat.parse(stringReader, true);
    }


    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class MessageFormat {
        private final String contents;
        private final ClientMessageArgumentType.MessageSelector[] selectors;

        public MessageFormat(String contents, ClientMessageArgumentType.MessageSelector[] selectors) {
            this.contents = contents;
            this.selectors = selectors;
        }

        public String getContents() {
            return this.contents;
        }

        public ClientMessageArgumentType.MessageSelector[] getSelectors() {
            return this.selectors;
        }

        public Text format(FabricClientCommandSource source, boolean canUseSelectors) throws CommandSyntaxException {
            if (this.selectors.length == 0 || !canUseSelectors) {
                return new LiteralText(this.contents);
            }

            MutableText mutableText = new LiteralText(this.contents.substring(0, this.selectors[0].getStart()));
            int i = this.selectors[0].getStart();

            for (ClientMessageArgumentType.MessageSelector messageSelector : this.selectors) {
                if (i < messageSelector.getStart()) {
                    mutableText.append(this.contents.substring(i, messageSelector.getStart()));
                }

                i = messageSelector.getEnd();
            }

            if (i < this.contents.length()) {
                mutableText.append(this.contents.substring(i));
            }

            return mutableText;
        }

        public static MessageFormat parse(StringReader reader, boolean canUseSelectors) throws CommandSyntaxException {
            String string = reader.getString().substring(reader.getCursor(), reader.getTotalLength());

            if (!canUseSelectors) {
                reader.setCursor(reader.getTotalLength());
                return new MessageFormat(string, new ClientMessageArgumentType.MessageSelector[0]);
            }

            List<ClientMessageArgumentType.MessageSelector> list = Lists.newArrayList();
            int i = reader.getCursor();

            while (reader.canRead()) {
                if (reader.peek() == '@') {
                    EntitySelector entitySelector; int j = reader.getCursor();

                    try {
                        EntitySelectorReader entitySelectorReader = new EntitySelectorReader(reader);
                        entitySelector = entitySelectorReader.read();
                    } catch (CommandSyntaxException commandSyntaxException) {
                        if (commandSyntaxException.getType() == EntitySelectorReader.MISSING_EXCEPTION || commandSyntaxException.getType() == EntitySelectorReader.UNKNOWN_SELECTOR_EXCEPTION) {
                            reader.setCursor(j + 1);
                            continue;
                        }
                        throw commandSyntaxException;
                    }
                    list.add(new ClientMessageArgumentType.MessageSelector(j - i, reader.getCursor() - i, entitySelector)); continue;
                }
                reader.skip();
            }


            return new MessageFormat(string, list.<ClientMessageArgumentType.MessageSelector>toArray(new ClientMessageArgumentType.MessageSelector[0]));
        }
    }

    public static class MessageSelector {
        private final int start;
        private final int end;
        private final EntitySelector selector;

        public MessageSelector(int start, int end, EntitySelector selector) {
            this.start = start;
            this.end = end;
            this.selector = selector;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public EntitySelector getSelector() {
            return this.selector;
        }


    }
}
