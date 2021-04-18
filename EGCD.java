import java.math.BigInteger;
import java.util.Scanner;

public class EGCD {

    public static void main(String[] args)
    {
        System.out.println("Extended Euclidean Algorithm Table Generator");

        BigInteger a = BigInteger.ZERO, b = BigInteger.ZERO;
        if (args.length != 0) {
            a = new BigInteger(args[0]);
            b = new BigInteger(args[1]);
        }
        else {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("Enter modulus (bigger number)");
                a = scan.nextBigInteger();
                System.out.println("Enter the smaller number:");
                b = scan.nextBigInteger();

                if(a.compareTo(BigInteger.ZERO) == 0 || b.compareTo(BigInteger.ZERO) == 0)
                    throw new Exception("No zeroes allowed.");

            }catch (Exception e) {
                System.out.println(e + "\nBad input");
                System.exit(666);
            }
        }

        ExtendedEuclid(a,b);
    }

    //extended euclidean to encrypt/decrypt messages.
    public static void ExtendedEuclid(BigInteger a, BigInteger b)
    {   //b is always the mod.

        System.out.println("\t\t\t\t===================== Extended Euclidean Algorithm =====================");
        System.out.printf("%s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t\t    %-8s\n","no","n1","n2","r","q","a1","b1","a2","b2","t");


        String baseA = a.toString(), baseB = b.toString();



        BigInteger mod = b;
        BigInteger n1 = a, n2 = b;
        BigInteger a1 = BigInteger.ONE, b1 = BigInteger.ZERO,
                a2 = BigInteger.ZERO, b2 = BigInteger.ONE;

        BigInteger quot = BigInteger.ZERO, rem = BigInteger.ZERO;
        BigInteger temp = BigInteger.ZERO;

        quot = n1.divide(n2); //n1 / n2;
        rem = n1.mod(n2);     //n1 % n2;


        String sn1,sn2,sr,sq,sa1,sb1,sa2,sb2,st1,st2;
        int line = 1;

        sn1 = n1.toString(); sn2 = n2.toString(); sr = rem.toString(); sq = quot.toString(); st1 = "0"; st2 = "0";
        sa1 = a1.toString(); sb1 = b1.toString(); sa2 = a2.toString(); sb2 = b2.toString();


        System.out.printf("%d\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%8s,%-8s\n",line,sn1,sn2,sr,sq,sa1,sb1,sa2,sb2,st1,st2);

        while (rem.compareTo(BigInteger.ZERO) > 0) //(rem != 0)
        {
            line++;

            n1 = n2;
            n2 = rem;
            temp = a2;
            st1 = temp.toString();
            a2 = a1.subtract(quot.multiply(a2)); //a1 - quot * a2;
            a1 = temp;
            temp = b2;
            st2 = temp.toString();
            b2 = b1.subtract(quot.multiply(b2)); //b1 - quot * b2;
            b1 = temp;

            quot = n1.divide(n2); //n1 / n2;
            rem = n1.mod(n2);     //n1 % n2;

            sn1 = n1.toString(); sn2 = n2.toString(); sr = rem.toString(); sq = quot.toString();
            sa1 = a1.toString(); sb1 = b1.toString(); sa2 = a2.toString(); sb2 = b2.toString();
            System.out.printf("%d\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%8s,%-8s\n",line,sn1,sn2,sr,sq,sa1,sb1,sa2,sb2,st1,st2);
        }

        System.out.printf("\n\ngcd(%s,%s) = %s\n",baseA,baseB,sn2);
        System.out.printf("\nExt. gcd(%s,%s) = %s x %s + %s x %s\n",baseA,baseB,baseA,sa2,baseB,sb2);

        BigInteger g = n2;
        a = a2;
        b = b2;
    }
}
