package ro.pippo.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.*;

/**
 * @author Alexander Brandt
 */
public class EmailObfuscationExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>();
        filters.put("obfuscate", new EmailObfuscationFilter());
        return filters;
    }

    public class EmailObfuscationFilter implements Filter {
        @Override
        public List<String> getArgumentNames() {
            return Collections.emptyList();
        }

        @Override
        public Object apply(Object input, Map<String, Object> args){
            if(input == null){
                return null;
            }
            String str = (String) input;
            return obfuscate(str);
        }

        /* The following code is taken from PegDown:
        https://github.com/sirthias/pegdown/blob/master/src/main/java/org/pegdown/FastEncoder.java
         */
        private Random random = new Random(0x2626);

        public String obfuscate(String email) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < email.length(); i++) {
                char c = email.charAt(i);
                switch (random.nextInt(5)) {
                    case 0:
                    case 1:
                        sb.append("&#").append((int) c).append(';');
                        break;
                    case 2:
                    case 3:
                        sb.append("&#x").append(Integer.toHexString(c)).append(';');
                        break;
                    case 4:
                        String encoded = encode(c);
                        if (encoded != null) sb.append(encoded); else sb.append(c);
                }
            }
            return sb.toString();
        }

        public String encode(char c) {
            switch (c) {
                case '&':  return "&amp;";
                case '<':  return "&lt;";
                case '>':  return "&gt;";
                case '"':  return "&quot;";
                case '\'': return "&#39;";
            }
            return null;
        }
    }

}
