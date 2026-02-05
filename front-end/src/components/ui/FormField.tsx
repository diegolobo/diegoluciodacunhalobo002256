import { InputHTMLAttributes, SelectHTMLAttributes, ReactNode } from 'react'

interface BaseFieldProps {
  label: string
  error?: string
  required?: boolean
}

interface InputFieldProps extends BaseFieldProps, InputHTMLAttributes<HTMLInputElement> {
  type?: 'text' | 'number' | 'email' | 'password'
}

interface SelectFieldProps extends BaseFieldProps, SelectHTMLAttributes<HTMLSelectElement> {
  children: ReactNode
}

export function InputField({ label, error, required, className = '', ...props }: InputFieldProps) {
  return (
    <div className="space-y-1">
      <label className="block text-sm font-medium text-dark-800 dark:text-cream-100">
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>
      <input
        {...props}
        className={`w-full px-4 py-2 rounded-lg border bg-white dark:bg-dark-700 text-dark-800 dark:text-cream-100 placeholder-dark-500 dark:placeholder-cream-200 focus:outline-none focus:ring-2 focus:ring-accent-gold transition-colors ${
          error
            ? 'border-red-500 dark:border-red-500'
            : 'border-cream-200 dark:border-dark-500'
        } ${className}`}
      />
      {error && <p className="text-sm text-red-500 dark:text-red-400">{error}</p>}
    </div>
  )
}

export function SelectField({ label, error, required, children, className = '', ...props }: SelectFieldProps) {
  return (
    <div className="space-y-1">
      <label className="block text-sm font-medium text-dark-800 dark:text-cream-100">
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>
      <select
        {...props}
        className={`w-full px-4 py-2 rounded-lg border bg-white dark:bg-dark-700 text-dark-800 dark:text-cream-100 focus:outline-none focus:ring-2 focus:ring-accent-gold transition-colors cursor-pointer ${
          error
            ? 'border-red-500 dark:border-red-500'
            : 'border-cream-200 dark:border-dark-500'
        } ${className}`}
      >
        {children}
      </select>
      {error && <p className="text-sm text-red-500 dark:text-red-400">{error}</p>}
    </div>
  )
}

interface CheckboxFieldProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label: string
}

export function CheckboxField({ label, className = '', ...props }: CheckboxFieldProps) {
  return (
    <label className="flex items-center gap-2 cursor-pointer">
      <input
        type="checkbox"
        {...props}
        className={`w-4 h-4 rounded border-cream-200 dark:border-dark-500 text-accent-gold focus:ring-accent-gold cursor-pointer ${className}`}
      />
      <span className="text-sm text-dark-800 dark:text-cream-100">{label}</span>
    </label>
  )
}
