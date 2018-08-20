interface CookieTools {
  /**
   *
   * @param {string} name
   * @param {string} value
   * @param option
   */
  set(name: string, value: string, option: any): void

  /**
   *
   * @param {string} name
   * @returns {string}
   */
  get(name: string): string;

  /**
   * 删除cookie
   * @param name
   */
  remove(name: string): void;
}

declare const Cookies: CookieTools;
