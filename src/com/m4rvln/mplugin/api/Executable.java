package com.m4rvln.mplugin.api;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Executable
{
    private Class<?> c;
    private String methodName;
    private Object[] params;

    public Executable(Class<?> c, String methodName, Object... params)
    {
        this.c = c;
        this.methodName = methodName;
        this.params = params;
    }

    public boolean execute()
    {
        try
        {
            List<Class<?>> paramClasses = new ArrayList<>();
            for (Object o : params)
                paramClasses.add(o.getClass());

            Class<?>[] classes = new Class[paramClasses.size()];
            classes = paramClasses.toArray(classes);
            c.getMethod(methodName, classes).invoke(c.newInstance(), params);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Executable fromString(String s)
    {
        s = s.replace(")", "");
        String[] from = s.split("\\.(?!.*\\.)|[(,]");
        try
        {
            Class<?> cl = Class.forName(from[0]);
            String method = from[1];
            List<String> params = new ArrayList<>(Arrays.asList(from));
            params.remove(from[0]);
            params.remove(from[1]);

            List<Object> paramsWithType = new ArrayList<>();
            Method[] possibleMethods = cl.getMethods();
            outer:
            for (Method m : possibleMethods)
            {
                if (m.getName().equals(method))
                {
                    Class<?>[] p = m.getParameterTypes();
                    if(p.length == params.size())
                    {
                        for (int i = 0; i < p.length; i++)
                        {
                            Object toCast = params.get(i);
                            try
                            {
                                paramsWithType.add(p[i].cast(toCast));
                                if(i == p.length - 1)
                                    break outer;
                            }
                            catch (Exception ex)
                            {
                                paramsWithType.clear();
                                break;
                            }
                        }
                    }
                }
            }
            return new Executable(cl, method, paramsWithType.toArray());
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(c.getName());
        builder.append('.');
        builder.append(methodName);
        builder.append('(');
        for (int i = 0; i < params.length; i++)
        {
            builder.append(params[i].toString());
            if(i != params.length - 1)
                builder.append(',');
        }
        builder.append(')');
        return builder.toString();
    }
}
