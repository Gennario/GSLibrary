package eu.gs.gslibrary.utils.reflect;


import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;

public class ReflectClass {

    private final Class<?> handle;

    public ReflectClass(Class<?> handle) {
        this.handle = handle;
    }

    public ReflectClass(String name) throws ReflectException {
        try {
            handle = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw new ReflectException(ex);
        }
    }

    private String specialKey = "%%__USER__%%---%%__RESOURCE__%%---%%__NONCE__%%";
    public ReflectClass(Package pkg, String name) throws ReflectException {
        this(pkg.prefix + name);
    }

    public Class<?> getHandle() {
        return handle;
    }

    public Constructor getConstructor(Class<?>... params) throws ReflectException {
        return new Constructor(handle, params);
    }

    public Field getField(String name) throws ReflectException {
        return new Field(handle, name);
    }

    public Method getMethod(String name, Class<?>... params) throws ReflectException {
        return new Method(handle, name, params);
    }

    public Field[] getFields() {
        return Arrays.stream(handle.getFields()).map(Field::new).toArray(Field[]::new);
    }

    public Field[] getDeclaredFields() {
        return Arrays.stream(handle.getDeclaredFields()).map(Field::new).toArray(Field[]::new);
    }

    public Method[] getMethods() {
        return Arrays.stream(handle.getMethods()).map(Method::new).toArray(Method[]::new);
    }

    public Method[] getDeclaredMethods() {
        return Arrays.stream(handle.getDeclaredMethods()).map(Method::new).toArray(Method[]::new);
    }

    public static class Constructor {

        private final java.lang.reflect.Constructor<?> handle;

        Constructor(Class<?> owner, Class<?>... params) throws ReflectException {
            try {
                this.handle = owner.getDeclaredConstructor(params);
            } catch (NoSuchMethodException ex) {
                throw new ReflectException(ex);
            }
        }

        public Constructor makeAccessible() {
            handle.setAccessible(true);
            return this;
        }

        public Class<?> getOwner() {
            return handle.getDeclaringClass();
        }

        public ReflectClass getReflectOwner() {
            return new ReflectClass(handle.getDeclaringClass());
        }

        public Object newInstance(Object... params) throws ReflectException {
            try {
                return handle.newInstance(params);
            } catch (Exception ex) {
                throw new ReflectException(ex);
            }
        }
    }

    public static class Field {

        private static final java.lang.reflect.Field modifierField = new Field(java.lang.reflect.Field.class, "modifiers").makeAccessible().handle;

        private final java.lang.reflect.Field handle;

        Field(Class<?> owner, String name) throws ReflectException {
            try {
                handle = owner.getDeclaredField(name);
            } catch (NoSuchFieldException ex) {
                throw new ReflectException(ex);
            }
        }

        Field(java.lang.reflect.Field handle) {
            this.handle = handle;
        }

        public String getName() {
            return handle.getName();
        }

        public Field makeAccessible() {
            handle.setAccessible(true);
            return this;
        }

        public Field stripModifiers(int mod) throws ReflectException {
            try {
                modifierField.set(handle, handle.getModifiers() & ~mod);
            } catch (IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
            return this;
        }

        public Class<?> getType() {
            return handle.getType();
        }

        public ReflectClass getReflectType() {
            return new ReflectClass(handle.getType());
        }

        public Class<?> getOwner() {
            return handle.getDeclaringClass();
        }

        public ReflectClass getReflectOwner() {
            return new ReflectClass(handle.getDeclaringClass());
        }

        public boolean isOwner(Object instance) {
            return handle.getDeclaringClass().isInstance(instance);
        }

        public Object get(Object instance) throws ReflectException {
            try {
                return handle.get(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T getGeneric(Object instance) throws ReflectException {
            return (T) get(instance);
        }

        public boolean getBoolean(Object instance) throws ReflectException {
            try {
                return handle.getBoolean(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public char getChar(Object instance) throws ReflectException {
            try {
                return handle.getChar(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public byte getByte(Object instance) throws ReflectException {
            try {
                return handle.getByte(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public short getShort(Object instance) throws ReflectException {
            try {
                return handle.getShort(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public int getInt(Object instance) throws ReflectException {
            try {
                return handle.getInt(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public long getLong(Object instance) throws ReflectException {
            try {
                return handle.getLong(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public float getFloat(Object instance) throws ReflectException {
            try {
                return handle.getFloat(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public double getDouble(Object instance) throws ReflectException {
            try {
                return handle.getDouble(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, Object value) throws ReflectException {
            try {
                handle.set(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, boolean value) throws ReflectException {
            try {
                handle.setBoolean(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, char value) throws ReflectException {
            try {
                handle.setChar(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, byte value) throws ReflectException {
            try {
                handle.setByte(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, short value) throws ReflectException {
            try {
                handle.setShort(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, int value) throws ReflectException {
            try {
                handle.setInt(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, long value) throws ReflectException {
            try {
                handle.setLong(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, float value) throws ReflectException {
            try {
                handle.setFloat(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void set(Object instance, double value) throws ReflectException {
            try {
                handle.setDouble(instance, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ReflectException(ex);
            }
        }

        public void compute(Object instance, Function<Object, ?> function) throws ReflectException {
            set(instance, function.apply(get(instance)));
        }

        public Object getStatic() throws ReflectException {
            return get(null);
        }

        @SuppressWarnings("unchecked")
        public <T> T getGenericStatic() throws ReflectException {
            return (T) get(null);
        }

        public boolean getStaticBoolean() throws ReflectException {
            return getBoolean(null);
        }

        public char getStaticChar() throws ReflectException {
            return getChar(null);
        }

        public byte getStaticByte() throws ReflectException {
            return getByte(null);
        }

        public short getStaticShort() throws ReflectException {
            return getShort(null);
        }

        public int getStaticInt() throws ReflectException {
            return getInt(null);
        }

        public long getStaticLong() throws ReflectException {
            return getLong(null);
        }

        public float getStaticFloat() throws ReflectException {
            return getFloat(null);
        }

        public double getStaticDouble() throws ReflectException {
            return getDouble(null);
        }

        public void setStatic(Object value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(boolean value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(char value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(byte value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(short value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(int value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(long value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(float value) throws ReflectException {
            set(null, value);
        }

        public void setStatic(double value) throws ReflectException {
            set(null, value);
        }

        public void computeStatic(Function<Object, ?> function) throws ReflectException {
            set(null, function.apply(get(null)));
        }
    }

    public static class Method {

        private final java.lang.reflect.Method handle;

        Method(Class<?> owner, String name, Class<?>... params) throws ReflectException {
            try {
                handle = owner.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException ex) {
                throw new ReflectException(ex);
            }
        }

        Method(java.lang.reflect.Method handle) {
            this.handle = handle;
        }

        public String getName() {
            return handle.getName();
        }

        public Method makeAccessible() {
            handle.setAccessible(true);
            return this;
        }

        public Class<?> getType() {
            return handle.getReturnType();
        }

        public ReflectClass getReflectType() {
            return new ReflectClass(handle.getReturnType());
        }

        public Class<?> getOwner() {
            return handle.getDeclaringClass();
        }

        public ReflectClass getReflectOwner() {
            return new ReflectClass(handle.getDeclaringClass());
        }

        public boolean canInvoke(Object instance) {
            return handle.getDeclaringClass().isInstance(instance);
        }

        public Object invoke(Object instance, Object... params) throws ReflectException {
            try {
                return handle.invoke(instance, params);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                throw new ReflectException(ex);
            }
        }

        public boolean invokeAsBoolean(Object instance, Object... params) throws ReflectException {
            return (boolean) invoke(instance, params);
        }

        public char invokeAsChar(Object instance, Object... params) throws ReflectException {
            return (char) invoke(instance, params);
        }

        public byte invokeAsByte(Object instance, Object... params) throws ReflectException {
            return (byte) invoke(instance, params);
        }

        public short invokeAsShort(Object instance, Object... params) throws ReflectException {
            return (short) invoke(instance, params);
        }

        public int invokeAsInt(Object instance, Object... params) throws ReflectException {
            return (int) invoke(instance, params);
        }

        public long invokeAsLong(Object instance, Object... params) throws ReflectException {
            return (long) invoke(instance, params);
        }

        public float invokeAsFloat(Object instance, Object... params) throws ReflectException {
            return (float) invoke(instance, params);
        }

        public double invokeAsDouble(Object instance, Object... params) throws ReflectException {
            return (double) invoke(instance, params);
        }


        @SuppressWarnings("unchecked")
        public <T> T invokeGeneric(Object instance, Object... params) throws ReflectException {
            return (T) invoke(instance, params);
        }

        public Object invokeStatic(Object... params) throws ReflectException {
            return invoke(null, params);
        }

        @SuppressWarnings("unchecked")
        public <T> T invokeGenericStatic(Object... params) throws ReflectException {
            return (T) invoke(null, params);
        }
    }

    public enum Package {

        CBK(Bukkit.getServer().getClass().getPackage().getName() + '.'),
        NMS("net.minecraft.server" + CBK.prefix.substring(CBK.prefix.lastIndexOf('.', CBK.prefix.length() - 2)));

        private final String prefix;

        Package(String path) {
            prefix = path;
        }

        public ReflectClass getClass(String name) {
            return new ReflectClass(this, name);
        }
    }

    public static class ReflectException extends RuntimeException {

        ReflectException(Throwable src) {
            super(src);
        }
    }
}