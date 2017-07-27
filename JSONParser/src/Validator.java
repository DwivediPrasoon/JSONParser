import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Validator {
	
	
	int line_number=0;
	int index=0;
	public void detectGrammer() throws Exception {
		String sCurrentLine= readFile("../JSONParser/JSON/example2.json",Charset.defaultCharset());
			index=0;
			ignorespaces(sCurrentLine);
			if(sCurrentLine.charAt(index)=='{') {
				detectobject(sCurrentLine);
				//System.out.print("s");
		
			
		}
	}
	String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	public void detectobject(String s) throws Exception {
		ignorespaces(s);
		
		if(s.charAt(index)=='{') {
				index++;
				while(true) {
				ignorespaces(s);
				//System.out.println("WATCH THIS"+s.charAt(index));
				if(s.charAt(index)=='}') {
					index++;
					return;}
				else if(s.charAt(index)=='"') {
					detectPair(s);
				}
				else if(s.charAt(index)==',') {
					index++;
					detectPair(s);
				}
				else
					throw new Exception("Invalid Object at line_number: "+line_number);
			}
						
		}
	}
	public void detectPair(String s) throws Exception{
		ignorespaces(s);
		//System.out.print(s.charAt(index));
		if(s.charAt(index)=='"') {
			detectString(s);
			ignorespaces(s);
			if(s.charAt(index)==':') {
				index++;
				detectValue(s);}
			else 
				throw new Exception("Value not found at line_number: "+line_number);
			return;
		}else
			throw new Exception("No Pair Exists at line_number: "+line_number);
	}
	public void detectValue(String s) throws Exception {
		ignorespaces(s);
		//System.out.print(s.charAt(index));
		if(s.charAt(index)=='"') {
		detectString(s);
		}
		else if(s.charAt(index)=='t')
			index+=4;
		else if(s.charAt(index)=='f')
			index+=5;
		
		else if(s.charAt(index)=='{') {
			ignorespaces(s);
			detectobject(s);
		}
		else if(s.charAt(index)=='[') {
			detectArray(s);
		}
		else if(s.charAt(index)=='\0') {
			return ;
		}
		else if(s.charAt(index)<='9' && s.charAt(index)>='1')
			parseNumber(s);
		else {
			throw new Exception("Invalid Value at line_number: "+line_number);
		}

		
	}
	public void parseNumber(String s) throws Exception {

        parseInt(s);
        if (s.charAt(index) == '.') {
            // TODO fraction
            // can be followed by exponent.
            parseInt(s);
            if (s.charAt(index) == 'e' || s.charAt(index) == 'E') {
                index++;
                if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                    index += 1;
                    parseInt(s);
                    // take some action
                    return;
                }
            }
        } else if (s.charAt(index) == 'e' || s.charAt(index) == 'E') {
            // TODO exponentiation
            // just check the next character n then parseDigits()
            index += 1;
            if (s.charAt(index) == '+' || s.charAt(index) == '-') {
                index += 1;
                parseInt(s);
                // take some action
                return;
            }
        }
        ignorespaces(s);
        if (s.charAt(index) == ',' || s.charAt(index)=='}') {
            return;
        } else {
            throw new Exception(", or } expected at position " + line_number);

        }
    }

    public void parseInt(String s) {
        // parseInt
        // can start with digit or - sign
        if(Character.isDigit(s.charAt(index)) || s.charAt(index) == '-') {
            index+= 1;
            while(Character.isDigit(s.charAt(index))){
                index+= 1;
            }
        }
    }
	public void detectArray(String s)throws Exception {
		ignorespaces(s);
		if(s.charAt(index)=='[') {
			index++;
			while(true) {
				ignorespaces(s);
				
				if(s.charAt(index)==']') {
					index++;
					return;}
				else if(s.charAt(index)=='"')
					detectString(s);
				else if(s.charAt(index)==','){
					index++;
					ignorespaces(s);
					if(s.charAt(index)=='"')
						detectString(s);
					else
						throw new Exception("String not foundat line_number: "+line_number);
				}
				else
					throw new Exception("Invalid Arrayat line_number: "+line_number);
			}
		}
	}
	

	public void detectString(String s) throws Exception {
		
		if(s.charAt(index)=='"') {
			index++;
			while( index<s.length() && (s.charAt(index)!='"'  &&
					s.charAt(index)!='\\' && 
					s.charAt(index)!='\b' &&
					s.charAt(index)!='\f' &&
					s.charAt(index)!='\n' &&
					s.charAt(index)!='\r' &&
					s.charAt(index)!='\t' 
					) ){
					//System.out.print(s.charAt(index));
					index++;
				}
			
			
			if(s.charAt(index)=='"') {
				index++;
				return;
				}
			else
				throw new Exception("\" not found at line_number: "+line_number);
		}
		else {
			throw new Exception("Illegal String at line_number: "+line_number);
		}
		
	}
	
	public void ignorespaces(String s) {
			while((s.charAt(index)==' ' || s.charAt(index)=='\n' || s.charAt(index)=='\t')&& index<s.length()) {
			//System.out.print(s.charAt(index));
				if(s.charAt(index)=='\n')
					line_number++;
			index++;
		}
	}

}