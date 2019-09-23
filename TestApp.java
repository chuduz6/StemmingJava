
import java.lang.reflect.Method;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class TestApp {
    private static void usage()
    {
        System.err.println("Usage: TestApp <algorithm> <input file> [-o <output file>]");
    }

    public static void main(String [] args) throws Throwable {
	if (args.length < 1) {
            usage();
            return;
        }

	englishStemmer stemClass = new englishStemmer();
        SnowballStemmer stemmer = (SnowballStemmer) stemClass;

	Reader reader;
	reader = new InputStreamReader(new FileInputStream(args[0]));
	reader = new BufferedReader(reader);

	StringBuffer input = new StringBuffer();

    OutputStream outstream;

	if (args.length > 1) {
            if (args.length >= 3 && args[1].equals("-o")) {
                outstream = new FileOutputStream(args[2]);
            } else {
                usage();
                return;
            }
	} else {
	    outstream = System.out;
	}
	Writer output = new OutputStreamWriter(outstream);
	output = new BufferedWriter(output);

	int repeat = 1;
	if (args.length > 3) {
	    repeat = Integer.parseInt(args[3]);
	}

	Object [] emptyArgs = new Object[0];
	int character;
	while ((character = reader.read()) != -1) {
	    char ch = (char) character;
	    if (Character.isWhitespace((char) ch)) {
		if (input.length() > 0) {
		    stemmer.setCurrent(input.toString());
		    for (int i = repeat; i != 0; i--) {
			stemmer.stem();
		    }
		    output.write(stemmer.getCurrent());
		    output.write(' ');
		    input.delete(0, input.length());
		}
	    } else {
		input.append(Character.toLowerCase(ch));
	    }
	}
	
	if (input.length() > 0) {
	    stemmer.setCurrent(input.toString());
	    for (int i = repeat; i != 0; i--) {
		stemmer.stem();
	    }
	    output.write(stemmer.getCurrent());
	    output.write(' ');
	    input.delete(0, input.length());
	}
	
	output.write('\n');
	output.write("done writing");
	output.flush();
    }
}
